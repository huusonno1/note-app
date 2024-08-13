package internsafegate.noteapp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "note_content")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteContent extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content_type")
    private ContentType contentType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "text_content")
    private String textContent;

    @Column(name = "status_note_content")
    @Enumerated(EnumType.STRING)
    private NoteStatus statusNoteContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Notes notes;
}
