package com.workintech.zoo.controller;

import com.workintech.zoo.entity.Koala;
import com.workintech.zoo.exceptions.ZooException;
import com.workintech.zoo.exceptions.ZooErrorResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/koalas")
@Slf4j
public class KoalaController {

    private Map<Integer, Koala> koalas;

    @PostConstruct
    public void init(){
        this.koalas= new HashMap<>();
    }

    @GetMapping
    public List<Koala> find() {
        return this.koalas.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Koala find(@PathVariable Integer id) {
        if (!koalas.containsKey(id)) {
            String errorMessage = "Koala with id " + id + " not found";
            log.error(errorMessage);
            throw new ZooException(errorMessage, HttpStatus.NOT_FOUND);
        }
        return koalas.get(id);
    }

    @PostMapping
    public Koala save(@RequestBody Koala koala) {
        koalas.put(koala.getId(), koala);
        return koalas.get(koala.getId());
    }

    @PutMapping("/{id}")
    public Koala updateKoala(@PathVariable Integer id, @RequestBody Koala updatedKoala) {
        if (!koalas.containsKey(id)) {
            String errorMessage = "Koala with id " + id + " not found";
            log.error(errorMessage);
            throw new ZooException(errorMessage, HttpStatus.NOT_FOUND);
        }
        koalas.put(id, updatedKoala);
        return updatedKoala;
    }

    @DeleteMapping("/{id}")
    public void deleteKoala(@PathVariable Integer id) {
        if (!koalas.containsKey(id)) {
            String errorMessage = "Koala with id " + id + " not found";
            log.error(errorMessage);
            throw new ZooException(errorMessage, HttpStatus.NOT_FOUND);
        }
        koalas.remove(id);
    }

    @ExceptionHandler(ZooException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ZooErrorResponse handleZooException(ZooException ex) {
        return new ZooErrorResponse(ex.getHttpStatus().value(),ex.getMessage(), System.currentTimeMillis());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ZooErrorResponse handleException(Exception ex) {
        log.error("Internal Server Error: " + ex.getMessage());
        return new ZooErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Internal Server Error",  System.currentTimeMillis());
    }
}
