package com.comet.mudleserver.response

import lombok.ToString

@ToString
class ObjectResponse<T>(message: String, val content : T) : Response(message) {
}