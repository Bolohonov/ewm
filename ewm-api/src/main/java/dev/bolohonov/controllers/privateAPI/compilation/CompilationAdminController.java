package dev.bolohonov.controllers.privateAPI.compilation;

import dev.bolohonov.server.dto.compilation.CompilationAddDto;
import dev.bolohonov.server.dto.compilation.CompilationDto;
import dev.bolohonov.server.services.CompilationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/compilations")
@Validated
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(OK)
    public Optional<CompilationDto> addNewCompilation(
            @RequestBody @Valid CompilationAddDto compilation) {
        log.debug("Пришел запрос на добавление новой подборки");
        return compilationService.addCompilation(compilation);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(OK)
    public void removeCompilation(@PathVariable Long compId) {
        log.debug("Пришел запрос на удаление подборки");
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    @ResponseStatus(OK)
    public void removeEventFromCompilation(@PathVariable Long compId,
                                           @PathVariable Long eventId) {
        log.debug("Пришел запрос на удаление события из подборки");
        if (!compilationService.removeEventFromCompilation(compId, eventId)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    @PatchMapping("/{compId}/events/{eventId}")
    @ResponseStatus(OK)
    public void addEventToCompilation(@PathVariable Long compId,
                                      @PathVariable Long eventId) {
        log.debug("Пришел запрос на добавление события в подборку");
        if (!compilationService.addEventToCompilation(compId, eventId)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    @DeleteMapping("/{compId}/pin")
    @ResponseStatus(OK)
    public void removeCompilationFromHomePage(@PathVariable Long compId) {
        log.debug("Пришел запрос на удаление события с главной страницы");
        if (!compilationService.removeCompilationFromHomePage(compId)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }

    @PatchMapping("/{compId}/pin")
    @ResponseStatus(OK)
    public void addCompilationToHomePage(@PathVariable Long compId) {
        log.debug("Пришел запрос на добавление события на главную страницу");
        if (!compilationService.addCompilationToHomePage(compId)) {
            throw new ResponseStatusException(BAD_REQUEST);
        }
    }
}
