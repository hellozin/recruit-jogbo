package univ.study.recruitjogbo.tip;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import univ.study.recruitjogbo.api.request.TipPublishRequest;
import univ.study.recruitjogbo.error.NotFoundException;
import univ.study.recruitjogbo.error.UnauthorizedException;
import univ.study.recruitjogbo.member.Member;
import univ.study.recruitjogbo.member.MemberService;
import univ.study.recruitjogbo.message.RabbitMQ;
import univ.study.recruitjogbo.message.TipEvent;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TipService {

    private final TipRepository tipRepository;

    private final MemberService memberService;

    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Tip publish(Long authorId, TipPublishRequest tipPublishRequest) {
        Member author = memberService.findById(authorId)
                .orElseThrow(() -> new NotFoundException(Member.class, authorId.toString()));
        Tip tip = save(new Tip(author, tipPublishRequest.getTitle(), tipPublishRequest.getContent()));
        try {
            rabbitTemplate.convertAndSend(RabbitMQ.EXCHANGE, RabbitMQ.TIP_CREATE,
                    new TipEvent(tip.getId(), tip.getTitle(), tip.getAuthor()));
        } catch (AmqpException exception) {
            log.warn("Fail MQ send tip publish message. {}", exception.getMessage());
        }
        return tip;
    }

    @Transactional
    public Tip edit(Long authorId, Long tipId, TipPublishRequest tipPublishRequest) {
        Tip tip = findById(tipId)
                .orElseThrow(() -> new NotFoundException(Tip.class, tipId.toString()));
        if (!tip.getAuthor().getId().equals(authorId)) {
            throw new UnauthorizedException("Author does not match.");
        }
        tip.edit(tipPublishRequest.getTitle(), tipPublishRequest.getContent());
        Tip edited = save(tip);
        try {
            rabbitTemplate.convertAndSend(RabbitMQ.EXCHANGE, RabbitMQ.TIP_UPDATE,
                    new TipEvent(edited.getId(), edited.getTitle(), edited.getAuthor()));
        } catch (AmqpException exception) {
            log.warn("Fail MQ send tip edit message. {}", exception.getMessage());
        }
        return save(tip);
    }

    @Transactional
    public void delete(Long authorId, Long tipId) {
        Tip tip = findById(tipId)
                .orElseThrow(() -> new NotFoundException(Tip.class, tipId.toString()));
        if (!tip.getAuthor().getId().equals(authorId)) {
            throw new UnauthorizedException("Author does not match.");
        }
        delete(tip);
    }

    @Transactional(readOnly = true)
    public Optional<Tip> findById(Long tipId) {
        return tipRepository.findById(tipId);
    }

    @Transactional(readOnly = true)
    public Page<Tip> findAll(Pageable pageable) {
        return tipRepository.findAll(pageable);
    }

    @Transactional
    protected Tip save(Tip tip) {
        return tipRepository.save(tip);
    }

    @Transactional
    protected void delete(Tip tip) {
        tipRepository.delete(tip);
    }

}
