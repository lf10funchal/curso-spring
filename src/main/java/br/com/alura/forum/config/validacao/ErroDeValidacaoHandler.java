package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //CHAMADO QUANDO DA UM ERRO NO CONTROLLER
public class ErroDeValidacaoHandler {
	
	@Autowired
	private MessageSource message; //AJUDA A PEGAR MENSAGENS DE ERROS

	@ResponseStatus(code = HttpStatus.BAD_REQUEST) //RETORNA 400 PARA O CLIENTE
	@ExceptionHandler(MethodArgumentNotValidException.class) //TIPO DE ERRO QUE ESTAMOS TRATANDO NO CASO, VALIDAÇÃO DE CAMPOS
	public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) {
		
		List<ErroDeFormularioDto> erroDto = new ArrayList<ErroDeFormularioDto>(); //ERRO DTO PARA RETORNAR SOMENTE O NECESSARIO PARA O CLIENTE, SEM ISSO É RETORNADO UM JSON GIGANTE
		List<FieldError> errors = exception.getBindingResult().getFieldErrors(); //BUSCA OS ERROS QUE VEIO DO CONTROLLER
		
		errors.forEach( e -> {
			String mensagem = message.getMessage(e, LocaleContextHolder.getLocale()); //PASSA COMO PARAMETRO O ERRO E O GET LOCALE PARA PEGAR A TRADUÇÃO CORRETA
			ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(),mensagem);
			erroDto.add(erro);
		});
		
		return erroDto; //RETORNA O ERRO DE FORMA SIMPLIFICADA
	}
}
