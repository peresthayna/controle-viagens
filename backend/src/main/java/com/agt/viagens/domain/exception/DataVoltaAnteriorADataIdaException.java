package com.agt.viagens.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class DataVoltaAnteriorADataIdaException extends RuntimeException {
	public DataVoltaAnteriorADataIdaException() {
        super("Data Volta não pode ser anterior à Data Ida.");
    }
}
