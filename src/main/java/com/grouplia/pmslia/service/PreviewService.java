package com.grouplia.pmslia.service;

import com.grouplia.pmslia.service.domain.PreviewConfig;
import com.grouplia.pmslia.service.domain.PreviewException;
import com.grouplia.pmslia.service.domain.PreviewResult;

public interface PreviewService {

	PreviewResult process(PreviewConfig config) throws PreviewException;

}
