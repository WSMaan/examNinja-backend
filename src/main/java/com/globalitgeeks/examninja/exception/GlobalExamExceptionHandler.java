package com.globalitgeeks.examninja.exception;



        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.MethodArgumentNotValidException;
        import org.springframework.web.bind.annotation.ExceptionHandler;
        import org.springframework.web.bind.annotation.RestControllerAdvice;
        import org.springframework.validation.ObjectError;

        import java.util.stream.Collectors;

        @RestControllerAdvice
    public class GlobalExamExceptionHandler {

        // Handle InvalidExamRequestException
        @ExceptionHandler(InvalidExamRequestException.class)
        public ResponseEntity<String> handleInvalidRequestException(InvalidExamRequestException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Handle InvalidDataException
        @ExceptionHandler(InvalidExamDataException.class)
        public ResponseEntity<String> handleInvalidDataException(InvalidExamDataException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        // Handle DatabaseOperationException
        @ExceptionHandler(ExamDataBaseOperationException.class)
        public ResponseEntity<String> handleDatabaseOperationException(ExamDataBaseOperationException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }

        // Handle QuestionNotFoundException
        @ExceptionHandler(QuestionNotFoundException.class)
        public ResponseEntity<String> handleQuestionNotFoundException(QuestionNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // 404 Not Found
        }

        // Handle MethodArgumentNotValidException
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
            String errorMessage = ex.getBindingResult().getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        // Handle generic exceptions (optional, catches any unhandled exceptions)
        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGenericException(Exception ex) {
            return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

