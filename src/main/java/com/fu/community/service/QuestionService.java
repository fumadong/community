package com.fu.community.service;

import com.fu.community.dto.PaginationDTO;
import com.fu.community.dto.QuestionDTO;
import com.fu.community.exception.CustomizeErrorCode;
import com.fu.community.exception.CustomizeException;
import com.fu.community.mapper.QuestionExtMapper;
import com.fu.community.mapper.QuestionMapper;
import com.fu.community.mapper.UserMapper;
import com.fu.community.model.Question;
import com.fu.community.model.QuestionExample;
import com.fu.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(Integer page, Integer size){

        //计算页数的偏移量
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPages;
        Integer totalCount =(int) questionMapper.countByExample(new QuestionExample());
        //计算页码总数
        if(totalCount%size==0){
            totalPages=totalCount/size;
        }else{
            totalPages = totalCount/size+1;
        }
        if(page<1){
            page=1;
        }
        if(page>totalPages){
            page=totalPages;
        }

        paginationDTO.setPagination(totalPages,page);

        Integer offset = size*(page-1);
        //每一页的列表
        //List<Question> questions = questionMapper.list(offset,size);

        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOSList = new ArrayList<>();


        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOSList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOSList);
        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {

        //计算页数的偏移量
        PaginationDTO paginationDTO = new PaginationDTO();

        Integer totalPages;
        //Integer totalCount = questionMapper.countByUserId(userId);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andIdEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);

        //计算页码总数
        if(totalCount%size==0){
            totalPages=totalCount/size;
        }else{
            totalPages = totalCount/size+1;
        }
        if(page<1){
            page=1;
        }
        if(page>totalPages){
            page=totalPages;
        }

        //totalCount = questionMapper.count();
        paginationDTO.setPagination(totalPages,page);
        Integer offset = size*(page-1);
        //每一页的列表
        //List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOSList = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOSList.add(questionDTO);
        }

        paginationDTO.setData(questionDTOSList);
        return paginationDTO;
    }

    //根据用户id找问题
    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    //创建问题或更新问题
    public void createOrUpdate(Question question) {
        if(question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            questionMapper.insert(question);
        }else{
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            question.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if(updated!=1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //questionMapper.update(question);
        }

    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        //通过数据库自增方式 统计浏览数
        questionExtMapper.incView(question);
    }


    //获取相关问题
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if(StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }

        //将标签格以"|"进行分割
        String[] tags = StringUtils.split(queryDTO.getTag(), ',');
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));

        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        List<Question> questions = questionExtMapper.selectRelated(question);

        //map():用于映射每个元素到对应的结果 需要List对象
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());


        return questionDTOS;
    }
}
