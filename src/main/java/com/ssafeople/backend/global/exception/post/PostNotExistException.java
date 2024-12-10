package com.ssafeople.backend.global.exception.post;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class PostNotExistException extends SsafeopleException {
  public static final SsafeopleException EXCEPTION = new PostNotExistException();

  public PostNotExistException() { super(ErrorCode.INVALID_POST_ID); }
}
