package edu.cnm.deepdive.codebreaker.client.service

import edu.cnm.deepdive.codebreaker.client.dto.ErrorResponse

/**
 * Exception wrapper for an [ErrorResponse] returned from the Codebreaker web service.
 */
data class ApiException(val response: ErrorResponse) : RuntimeException()