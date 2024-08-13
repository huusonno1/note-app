package internsafegate.noteapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notes extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "is_pinned")
    private boolean isPinned;

    @Column(name = "status_notes")
    @Enumerated(EnumType.STRING)
    private NoteStatus statusNotes;

    @Column(name = "number_order")
    private Long numberOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Users user;
}
