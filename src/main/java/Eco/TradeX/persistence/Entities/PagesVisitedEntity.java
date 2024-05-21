package Eco.TradeX.persistence.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "pages_visited")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagesVisitedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private TraderEntity user;

    @ManyToOne
    @JoinColumn(name = "page_id", nullable = false)
    private PagesEntity page;

    @Column(name = "visited_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date visitedAt;
}
