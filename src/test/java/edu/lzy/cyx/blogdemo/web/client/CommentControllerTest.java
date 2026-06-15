package edu.lzy.cyx.blogdemo.web.client;

import edu.lzy.cyx.blogdemo.model.domain.Comment;
import edu.lzy.cyx.blogdemo.responsedata.ArticleResponseData;
import edu.lzy.cyx.blogdemo.service.ICommentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CommentControllerTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void anonymousVisitorCanPublishSimpleComment() {
        ICommentService commentService = mock(ICommentService.class);
        CommentController controller = new CommentController();
        ReflectionTestUtils.setField(controller, "commentService", commentService);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");

        ArticleResponseData response = controller.publishComment(
                request,
                12,
                " Alice ",
                "Hello <script>alert(1)</script>"
        );

        ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentService).publishComment(captor.capture());
        Comment saved = captor.getValue();

        assertThat(response.isSuccess()).isTrue();
        assertThat(saved.getArticleId()).isEqualTo(12);
        assertThat(saved.getAuthor()).isEqualTo("Alice");
        assertThat(saved.getIp()).isEqualTo("127.0.0.1");
        assertThat(saved.getStatus()).isEqualTo("approved");
        assertThat(saved.getContent()).contains("&lt;").doesNotContain("<script>");
    }
}
