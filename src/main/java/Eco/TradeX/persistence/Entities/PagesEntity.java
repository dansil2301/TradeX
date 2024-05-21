package Eco.TradeX.persistence.Entities;

import Eco.TradeX.domain.Statistics.Pages;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name = "pages")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_name")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Pages pageName;
}
