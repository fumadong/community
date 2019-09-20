package com.fu.community.service;

import com.fu.community.dto.PaginationDTO;
import com.fu.community.dto.QuestionDTO;
import com.fu.community.mapper.QuestionMapper;
import com.fu.community.mapper.UserMapper;
import com.fu.community.model.Question;
import com.fu.community.model.QuestionExample;
import com.fu.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

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
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO> questionDTOSList = new ArrayList<>();


        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOSList.add(questionDTO);
        }

        paginationDTO.setQuestions(questionDTOSList);
        return paginationDTO;
    }

    public PaginationDTO list(Integer userId, Integer page, Integer size) {

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

        paginationDTO.setQuestions(questionDTOSList);
        return paginationDTO;
    }

    //根据用户id找问题
    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.selectByPrimaryKey(id);
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
            questionMapper.insert(question);
        }else{
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            question.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            questionMapper.updateByExampleSelective(updateQuestion,example);
            //questionMapper.update(question);
        }
    }
}
