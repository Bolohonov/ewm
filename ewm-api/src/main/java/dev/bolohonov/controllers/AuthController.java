package dev.bolohonov.controllers;

import dev.bolohonov.server.dto.LoginSuccessDto;
import dev.bolohonov.server.errors.security.AuthException;
import dev.bolohonov.server.model.Token;
import dev.bolohonov.server.dto.LoginDto;
import dev.bolohonov.security.UserDetailsImpl;
import dev.bolohonov.security.jwt.JwtUtils;
import dev.bolohonov.server.services.RoleService;
import dev.bolohonov.server.services.TokenService;
import dev.bolohonov.server.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/${ewm.api.version}/auth")
@CrossOrigin(origins = {"${ewm.origin.localhost}"},
        allowedHeaders = {"Origin", "Authorization", "X-Requested-With", "Content-Type", "Accept", "Cookie"},
        allowCredentials = "true",
        methods = {RequestMethod.POST, RequestMethod.OPTIONS},
        maxAge = 3600)
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final RoleService roleService;

    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService, PasswordEncoder encoder,
                          JwtUtils jwtUtils, RoleService roleService, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.roleService = roleService;
        this.tokenService = tokenService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody LoginDto request, HttpServletRequest httpServletRequest) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            log.warn("Bad credentials for "+request.getUsername());
            throw new AuthException(request.getUsername());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authentication);

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("user", userService.getUserByName(request.getUsername()).get());

        session.setAttribute("token", tokenService.saveToken(Token.builder()
                .token_validity(true)
                .token_value(jwt)
                .user(userService.getUserByName(request.getUsername()).get())
                .build()));

        UserDetailsImpl details = (UserDetailsImpl) authentication.getPrincipal();

        List<GrantedAuthority> authorities = roleService.getRolesForUser(null).stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(LoginSuccessDto.builder()
                .username(details.getUsername())
                .token(jwt)
                .roles(authorities.stream().map(a -> a.getAuthority()).collect(Collectors.toList()))
                .build()
        );
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (session != null && authentication != null) {
            Token token = (Token) session.getAttribute("token");

            Token tokenFromDb = tokenService.findById(token.getId()).orElse(null);
            tokenFromDb.setToken_validity(false);
            tokenService.saveToken(tokenFromDb);

            session.invalidate();

            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return ResponseEntity.ok().build();
    }
}
