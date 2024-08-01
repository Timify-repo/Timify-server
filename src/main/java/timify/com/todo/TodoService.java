package timify.com.todo;

import static timify.com.common.apiPayload.code.status.ErrorStatus.NOT_TODO_OWNER;
import static timify.com.common.apiPayload.code.status.ErrorStatus.NO_SUBJECT_FOUND;
import static timify.com.common.apiPayload.code.status.ErrorStatus.NO_TODO_FOUND;
import static timify.com.common.apiPayload.code.status.ErrorStatus.STUDY_METHOD_NOT_FOUND;
import static timify.com.common.apiPayload.code.status.ErrorStatus.STUDY_PLACE_NOT_FOUND;
import static timify.com.common.apiPayload.code.status.ErrorStatus.STUDY_TYPE_NOT_FOUND;
import static timify.com.todo.dto.TodoRequest.todoRequest;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timify.com.common.apiPayload.code.status.ErrorStatus;
import timify.com.common.apiPayload.exception.handler.StudyHandler;
import timify.com.common.apiPayload.exception.handler.SubjectHandler;
import timify.com.common.apiPayload.exception.handler.TodoHandler;
import timify.com.member.domain.Member;
import timify.com.study.domain.StudyMethod;
import timify.com.study.domain.StudyPlace;
import timify.com.study.domain.StudyType;
import timify.com.study.repository.StudyMethodRepository;
import timify.com.study.repository.StudyPlaceRepository;
import timify.com.study.repository.StudyTypeRepository;
import timify.com.subject.domain.Subject;
import timify.com.subject.domain.SubjectStatus;
import timify.com.subject.repository.SubjectRepository;
import timify.com.todo.domain.Todo;
import timify.com.todo.dto.TodoRequest.copyTodoRequest;
import timify.com.todo.repository.TodoRepository;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final SubjectRepository subjectRepository;
    private final StudyTypeRepository studyTypeRepository;
    private final StudyMethodRepository studyMethodRepository;
    private final StudyPlaceRepository studyPlaceRepository;

    @Transactional
    public Todo insertTodo(Member member, Long subjectId, todoRequest request) {

        Subject subject = validateSubject(subjectId, member);
        validateSubjectIsActive(subject);

        StudyType studyType = validateStudyType(request.getStudyTypeId(), member);
        StudyMethod studyMethod = validateStudyMethod(request.getStudyMethodId(), member);
        StudyPlace studyPlace = validateStudyPlace(request.getStudyPlaceId(), member);

        Todo insertTodo = TodoConverter.toTodo(request, studyType, studyMethod, studyPlace);
        insertTodo.associateMember(member);
        insertTodo.associateSubject(subject);
        return todoRepository.save(insertTodo);
    }

    @Transactional(readOnly = true)
    public List<Todo> getTodoList(Member member, Long subjectId) {
        return todoRepository.findByMemberAndSubjectId(member, subjectId);
    }

    @Transactional
    public void deleteTodo(Member member, Long todoId) {

        Todo deleteTodo = validateTodoOwner(member, todoId);

        deleteTodo.disassociateMember(member);
        deleteTodo.disassociateSubject(deleteTodo.getSubject());
        todoRepository.delete(deleteTodo);
    }

    @Transactional
    public Todo updateTodo(Member member, Long todoId, todoRequest request) {

        Todo updateTodo = validateTodoOwner(member, todoId);
        validateSubjectIsActive(updateTodo.getSubject());

        StudyType studyType = validateStudyType(request.getStudyTypeId(), member);
        StudyMethod studyMethod = validateStudyMethod(request.getStudyMethodId(), member);
        StudyPlace studyPlace = validateStudyPlace(request.getStudyPlaceId(), member);

        updateTodo.updateContent(request.getContent());
        updateTodo.updateDate(request.getDate());
        updateTodo.updateStudyType(studyType);
        updateTodo.updateStudyMethod(studyMethod);
        updateTodo.updateStudyPlace(studyPlace);

        return todoRepository.save(updateTodo);
    }

    @Transactional
    public List<Todo> copyTodo(Member member, Long todoId, copyTodoRequest request) {

        Todo existingTodo = validateTodoOwner(member, todoId);
        Subject subject = existingTodo.getSubject();
        validateSubjectIsActive(subject);

        List<Todo> copiedTodos = new ArrayList<>();

        for (LocalDate date : request.getDates()) {
            Todo newTodo = Todo.builder()
                .content(existingTodo.getContent())
                .date(date)
                .status(existingTodo.getStatus())
                .studyType(existingTodo.getStudyType())
                .studyMethod(existingTodo.getStudyMethod())
                .studyPlace(existingTodo.getStudyPlace())
                .member(existingTodo.getMember())
                .subject(existingTodo.getSubject())
                .build();
            newTodo.associateMember(member);
            newTodo.associateSubject(subject);
            copiedTodos.add(todoRepository.save(newTodo));
        }

        return copiedTodos;
    }

    private void validateSubjectIsActive(Subject subject) {
        if (subject.getStatus() != SubjectStatus.ACTIVE) {
            throw new TodoHandler(ErrorStatus.MOVED_SUBJECT_RESTRICTION);
        }
    }

    private Subject validateSubject(Long subjectId, Member member) {
        Subject subject = subjectRepository.findById(subjectId)
            .orElseThrow(() -> new SubjectHandler(NO_SUBJECT_FOUND));

        if(subject.getMember() != member) {
            throw new TodoHandler(ErrorStatus.NOT_TODO_OWNER);
        }

        return subject;
    }

    private StudyType validateStudyType(Long studyTypeId, Member member) {

        if (studyTypeId == -1) {
            return null;
        }

        StudyType studyType = studyTypeRepository.findById(studyTypeId)
            .orElseThrow(() -> new TodoHandler(STUDY_TYPE_NOT_FOUND));

        if (studyType.getMember() != member) {
            throw new TodoHandler(ErrorStatus.NOT_STUDY_TYPE_OWNER);
        }

        return studyType;
    }

    private StudyMethod validateStudyMethod(Long studyMethodId, Member member) {

        if (studyMethodId == -1) {
            return null;
        }

        StudyMethod studyMethod = studyMethodRepository.findById(studyMethodId)
            .orElseThrow(() -> new TodoHandler(STUDY_METHOD_NOT_FOUND));

        if (studyMethod.getMember() != member) {
            throw new TodoHandler(ErrorStatus.NOT_STUDY_METHOD_OWNER);
        }

        return studyMethod;
    }

    private StudyPlace validateStudyPlace(Long studyPlaceId, Member member) {

        if (studyPlaceId == -1) {
            return null;
        }

        StudyPlace studyPlace = studyPlaceRepository.findById(studyPlaceId)
            .orElseThrow(() -> new TodoHandler(STUDY_PLACE_NOT_FOUND));

        if (studyPlace.getMember() != member) {
            throw new TodoHandler(ErrorStatus.NOT_STUDY_PLACE_OWNER);
        }

        return studyPlace;
    }

    private Todo validateTodoOwner(Member member, Long todoId) {
        Todo todo = todoRepository.findById(todoId)
            .orElseThrow(() -> new TodoHandler(NO_TODO_FOUND));
        if (!todo.getMember().equals(member)) {
            throw new StudyHandler(NOT_TODO_OWNER);
        }
        return todo;
    }
}
