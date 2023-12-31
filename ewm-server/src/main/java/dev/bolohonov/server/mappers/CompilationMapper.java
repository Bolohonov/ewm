package dev.bolohonov.server.mappers;

import dev.bolohonov.server.errors.ApiError;
import dev.bolohonov.server.model.Compilation;
import dev.bolohonov.server.dto.compilation.CompilationAddDto;
import dev.bolohonov.server.dto.compilation.CompilationDto;
import dev.bolohonov.server.model.Event;
import dev.bolohonov.server.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompilationMapper {
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto toCompilationDto(Compilation compilation) {
        log.debug("Получен запрос на конвертацию подборки");
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                compilation.getEvents()
        );
    }

    @Transactional
    public Collection<CompilationDto> toCompilationDto(Collection<Compilation> compilations) {
        return compilations.stream()
                .map(c -> toCompilationDto(c))
                .collect(Collectors.toList());
    }

    @Transactional
    public Compilation fromCompilationDto(CompilationDto compilationDto) {
        log.debug("Получен запрос на конвертацию в подборку");
        return new Compilation(
                compilationDto.getId(),
                compilationDto.getTitle(),
                compilationDto.getPinned(),
                compilationDto.getEvents()
        );
    }

    @Transactional
    public Compilation fromCompilationAddDto(CompilationAddDto compilation) {
        log.debug("Получен запрос на конвертацию в подборку");
        Set<Event> events = new HashSet<>();
        compilation.getEvents()
                .stream()
                .forEach(id -> events.add(eventRepository.findById(id).orElseThrow(
                        () -> new ApiError(HttpStatus.BAD_REQUEST, "Получен несуществующий id",
                                String.format("Получен несуществующий id %s события " +
                                        "при попытке добавить подборку", id)
                        ))));
        return new Compilation(
                compilation.getTitle(),
                compilation.getPinned(),
                events
        );
    }
}
