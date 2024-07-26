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
    public Todo insertTodo(Member member, Long subjectId, @Valid todoRequest request) {
        Subject subject = findSubjectById(subjectId);
        validateSubjectIsActive(subject);

        StudyType studyType = validateStudyType(request.getStudyTypeId());
        StudyMethod studyMethod = validateStudyMethod(request.getStudyMethodId());
        StudyPlace studyPlace = validateStudyPlace(request.getStudyPlaceId());

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
    public void deleteTodo(Member member, Long subjectId, Long todoId) {
        Todo deleteTodo = validateTodoOwner(member, todoId);
        findSubjectById(subjectId);

        deleteTodo.disassociateMember(member);
        deleteTodo.disassociateSubject(deleteTodo.getSubject());
        todoRepository.delete(deleteTodo);
    }

    @Transactional
    public Todo updateTodo(Member member, Long subjectId, Long todoId, @Valid todoRequest request) {
        Todo updateTodo = validateTodoOwner(member, todoId);
        Subject subject = findSubjectById(subjectId);
        validateSubjectIsActive(subject);

        StudyType studyType = validateStudyType(request.getStudyTypeId());
        StudyMethod studyMethod = validateStudyMethod(request.getStudyMethodId());
        StudyPlace studyPlace = validateStudyPlace(request.getStudyPlaceId());

        updateTodo.updateContent(request.getContent());
        updateTodo.updateDate(request.getDate());
        updateTodo.updateStudyType(studyType);
        updateTodo.updateStudyMethod(studyMethod);
        updateTodo.updateStudyPlace(studyPlace);

        return todoRepository.save(updateTodo);
    }

    @Transactional
    public List<Todo> copyTodo(Member member, Long subjectId, Long todoId,
        @Valid copyTodoRequest request) {
        Todo existingTodo = validateTodoOwner(member, todoId);
        Subject subject = findSubjectById(subjectId);
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

    private Subject findSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId)
            .orElseThrow(() -> new SubjectHandler(NO_SUBJECT_FOUND));
    }

    private void validateSubjectIsActive(Subject subject) {
        if (subject.getStatus() != SubjectStatus.ACTIVE) {
            throw new TodoHandler(ErrorStatus.MOVED_SUBJECT_RESTRICTION);
        }
    }

    private StudyType validateStudyType(Long studyTypeId) {
        return studyTypeRepository.findById(studyTypeId)
            .orElseThrow(() -> new TodoHandler(STUDY_TYPE_NOT_FOUND));
    }

    private StudyMethod validateStudyMethod(Long studyMethodId) {
        return studyMethodRepository.findById(studyMethodId)
            .orElseThrow(() -> new TodoHandler(STUDY_METHOD_NOT_FOUND));
    }

    private StudyPlace validateStudyPlace(Long studyPlaceId) {
        return studyPlaceRepository.findById(studyPlaceId)
            .orElseThrow(() -> new TodoHandler(STUDY_PLACE_NOT_FOUND));
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
