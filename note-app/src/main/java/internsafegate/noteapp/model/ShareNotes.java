package internsafegate.noteapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "share_notes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareNotes extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_contribution_accepted")
    private boolean is_contribution_accepted ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Notes notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Users sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Users receiver;
}
