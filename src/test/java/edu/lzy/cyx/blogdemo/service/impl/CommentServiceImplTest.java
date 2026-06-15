package edu.lzy.cyx.blogdemo.service.impl;

import edu.lzy.cyx.blogdemo.dao.CommentMapper;
import edu.lzy.cyx.blogdemo.dao.StatisticMapper;
import edu.lzy.cyx.blogdemo.model.domain.Comment;
import edu.lzy.cyx.blogdemo.model.domain.Statistic;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentServiceImplTest {

    @Test
    void publishingCommentIncrementsCommentCount() {
        CommentMapper commentMapper = mock(CommentMapper.class);
        StatisticMapper statisticMapper = mock(StatisticMapper.class);
        CommentServiceImpl service = new CommentServiceImpl();
        ReflectionTestUtils.setField(service, "commentMapper", commentMapper);
        ReflectionTestUtils.setField(service, "statisticMapper", statisticMapper);

        Comment comment = new Comment();
        comment.setArticleId(7);

        Statistic statistic = new Statistic();
        statistic.setArticleId(7);
        statistic.setHits(9);
        statistic.setCommentsNum(2);
        when(statisticMapper.selectStatisticWithArticleId(7)).thenReturn(statistic);

        service.publishComment(comment);

        ArgumentCaptor<Statistic> captor = ArgumentCaptor.forClass(Statistic.class);
        verify(commentMapper).publishComment(comment);
        verify(statisticMapper).updateArticleCommentWithId(captor.capture());
        verify(statisticMapper, never()).updateArticleHitsWithId(statistic);
        assertThat(captor.getValue().getCommentsNum()).isEqualTo(3);
        assertThat(captor.getValue().getHits()).isEqualTo(9);
    }
}
