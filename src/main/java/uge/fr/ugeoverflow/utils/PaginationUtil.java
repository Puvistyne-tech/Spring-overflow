package uge.fr.ugeoverflow.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import uge.fr.ugeoverflow.dto.question.OneQuestionDTO;
import uge.fr.ugeoverflow.dto.question.QuestionDTO;

import java.util.ArrayList;

public class PaginationUtil {
//inspiré par ce tuto https://vbmendes.medium.com/pagination-for-a-list-with-spring-pageable-26423455d0d3
    private PaginationUtil(){}

    public static  Page<OneQuestionDTO> paginateGivenList(final Pageable pageable, ArrayList<OneQuestionDTO> list) {
        int first = Math.min(Long.valueOf(pageable.getOffset()).intValue(), list.size());
        int last = Math.min(first + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(first, last), pageable, list.size());
    }

    public static Page<QuestionDTO> paginateGivenListBetter(final Pageable pageable, ArrayList<QuestionDTO> list) {
        int first = Math.min(Long.valueOf(pageable.getOffset()).intValue(), list.size());
        int last = Math.min(first + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(first, last), pageable, list.size());
    }
}
