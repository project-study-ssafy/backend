package com.ssafeople.backend.global.exception.upload;

import com.ssafeople.backend.global.exception.SsafeopleException;
import com.ssafeople.backend.global.response.failure.ErrorCode;

public class UploadFailedException extends SsafeopleException {
  public static final SsafeopleException EXCEPTION = new UploadFailedException();

  public UploadFailedException() { super(ErrorCode.UPLOAD_FAILED); }
}
