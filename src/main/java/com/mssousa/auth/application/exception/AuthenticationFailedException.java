package com.mssousa.auth.application.exception;

/**
 * Exceção lançada quando a autenticação falha.
 * 
 * Características:
 * - Sempre genérica para não expor detalhes internos
 * - Usada pela camada de Application
 * - Traduzida para BadCredentialsException pela camada de Infrastructure
 * 
 * Razões de falha (nunca expostas ao cliente):
 * - Usuário não encontrado
 * - Senha inválida
 * - Usuário bloqueado/inativo
 * - Erro inesperado
 * 
 * Segurança:
 * - Detalhes reais vão apenas para logs
 * - Cliente recebe apenas mensagem genérica
 */
public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException(String message) {
        super(message);
    }

    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}