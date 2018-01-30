package org.entando.entando.web.common.handlers;

import java.util.List;

import org.entando.entando.web.common.handlers.model.RestValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.agiletec.aps.system.exception.ApsSystemException;

@EnableWebMvc
@ControllerAdvice
public class ValidationHandler {

	private static final Logger logger = LoggerFactory.getLogger(ValidationHandler.class);

	//	@Autowired
	//	private MessageSource messageSource;

	//	@ExceptionHandler(MaxUploadSizeExceededException.class)
	//	@ResponseStatus(HttpStatus.BAD_REQUEST)
	//	@Produces("application/json")
	//	@ResponseBody
	//	public RestValidationError processMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
	//		logger.debug("Handling MaxUploadSizeExceededException exception");
	//		List<ObjectError> errors = new ArrayList<>();
	//		String errCode = "error.upload.size.max";
	//		errors.add(new ObjectError("uploadFile", new String[] { errCode }, new String[] { FileUtils.byteCountToDisplaySize(ex.getMaxUploadSize()) },
	//				errCode));
	//		RestValidationError errorResult = processAllErrors(null, errors);
	//		return errorResult;
	//	}

	//	@ExceptionHandler(PostValidationException.class)
	//	@ResponseStatus(HttpStatus.BAD_REQUEST)
	//	@Produces("application/json")
	//	@ResponseBody
	//	public RestValidationError processPostValidationEx(PostValidationException ex) {
	//		logger.debug("Handling PostValidationException exception");
	//		BindingResult result = ex.getBindingResult();
	//		RestValidationError errors = processAllErrors(result);
	//		return errors;
	//	}

	//	@ExceptionHandler(CustomValidationException.class)
	//	@ResponseStatus(HttpStatus.BAD_REQUEST)
	//	@Produces("application/json")
	//	@ResponseBody
	//	public RestValidationError processPostValidationEx(CustomValidationException ex) {
	//		logger.debug("Handling CustomValidationException exception");
	//		List<ObjectError> objectErrors = ex.getObjectErrors();
	//		List<FieldError> fieldErrors = ex.getFieldErrors();
	//		RestValidationError errors = processAllErrors(fieldErrors, objectErrors);
	//		return errors;
	//	}

	@ExceptionHandler(value = Throwable.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public RestValidationError processValidationError(Throwable ex) {
		logger.warn("KKKKKKKKKKKKK");
		//		BindingResult result = ex.getBindingResult();
		RestValidationError errors = processAllErrors(null);
		return errors;
	}

	@ExceptionHandler(value = ApsSystemException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public RestValidationError processValidationError(ApsSystemException ex) {
		logger.warn("YYYYYYYYYYYYYYYYYYYY");
		//		BindingResult result = ex.getBindingResult();
		RestValidationError errors = processAllErrors(null);
		return errors;
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public RestValidationError processValidationError(Exception ex) {
		logger.warn("XXXXXXXXXXXXXXXXXXXXXXXXX");
		//		BindingResult result = ex.getBindingResult();
		RestValidationError errors = processAllErrors(null);
		return errors;
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public RestValidationError processValidationError(MethodArgumentNotValidException ex) {
		logger.warn("PPPPPPPPPPPPPPPPPPPPPP");
		logger.debug("Handling form validation error");
		BindingResult result = ex.getBindingResult();
		RestValidationError errors = processAllErrors(result);
		return errors;
	}

	private RestValidationError processAllErrors(BindingResult result) {
		return processAllErrors(result.getFieldErrors(), result.getGlobalErrors());
	}

	private RestValidationError processAllErrors(List<FieldError> fieldErrors, List<ObjectError> objectErrors) {
		RestValidationError dto = new RestValidationError();
		processFieldErrors(dto, fieldErrors);
		processGlobalErrors(dto, objectErrors);
		return dto;
	}

	private RestValidationError processFieldErrors(RestValidationError dto, List<FieldError> fieldErrors) {
		if (null != fieldErrors) {
			for (FieldError fieldError : fieldErrors) {
				String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
				dto.addFieldError(fieldError.getObjectName(), fieldError.getField(), localizedErrorMessage);
			}
		}
		return dto;
	}

	private RestValidationError processGlobalErrors(RestValidationError dto, List<ObjectError> globalErrors) {
		if (null != globalErrors) {
			for (ObjectError globalError : globalErrors) {
				String localizedErrorMessage = resolveLocalizedErrorMessage(globalError);
				dto.addGlobalError(globalError.getObjectName(), localizedErrorMessage);
			}
		}
		return dto;
	}

	/**
	 * prova ad utilizzare il default message, altrimenti va sul default di
	 * hibernate
	 * 
	 * @param fieldError
	 * @return
	 */
	private String resolveLocalizedErrorMessage(DefaultMessageSourceResolvable fieldError) {
		//		Locale currentLocale = LocaleContextHolder.getLocale();
		//		String msgCode = StringUtils.isNotBlank(fieldError.getDefaultMessage()) ? fieldError.getDefaultMessage() : fieldError.getCode();
		//		String localizedErrorMessage = messageSource.getMessage(msgCode, fieldError.getArguments(), currentLocale);
		//		return localizedErrorMessage;
		return "todo";
	}
}
