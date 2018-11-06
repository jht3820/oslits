/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kr.opensoftlab.sdf.paging;

import egovframework.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;

/**
 * @Class Name : OslImgPaginationRenderer.java
 * @Description : OslImgPaginationRenderer 클래스
 * @Modification Information
 *
 * @author 안세웅
 * @since 2016.01.21.
 * @version 1.0
 * @see
 *  
 *  Copyright (C) OpenSoftLab Corp All right reserved.
 */
public class OslImgPaginationRenderer extends AbstractPaginationRenderer {

    /**
    * PaginationRenderer
    */
	public OslImgPaginationRenderer() {
		
		firstPageLabel =    "<a class=\"pre\" onclick=\"{0}({1}); return false; \">&lt;&lt;</a>";

		previousPageLabel = "<a class=\"pre\" onclick=\"{0}({1}); return false; \">&lt;</a>";
		
		currentPageLabel =  "<strong><span>{0}</span></strong>";
		
		otherPageLabel =    "<a onclick=\"{0}({1}); return false;\"><span>{2}</span></a>";
		
		nextPageLabel =     "<a class=\"next\" onclick=\"{0}({1}); return false;\">&gt;</a>";
		
		lastPageLabel =     "<a class=\"next\" onclick=\"{0}({1}); return false;\">&gt;&gt;</a>";

	}
}
