package com.jiuyi.doctor.frame;

import com.jiuyi.doctor.comment.Comment;

import junit.framework.TestCase;

public class FrameTest extends TestCase {

	public void testTest() {
		Comment cmt = new Comment();
		
		cmt.setCommentContent("test");
		
		assertNotNull(cmt.getCommentContent());
	}

}
