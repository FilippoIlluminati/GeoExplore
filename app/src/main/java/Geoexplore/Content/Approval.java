package Geoexplore.Content;

import jakarta.persistence.*;
import Geoexplore.User.Users;

@Entity
@Table(name = "approvals")
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @ManyToOne
    @JoinColumn(name = "approved_by", nullable = false)
    private Users approver;

    @Column(nullable = false)
    private Boolean isApproved;

    // Costruttore vuoto richiesto da JPA
    public Approval() {}

    // Costruttore con parametri
    public Approval(Content content, Users approver, Boolean isApproved) {
        this.content = content;
        this.approver = approver;
        this.isApproved = isApproved;
    }

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Users getApprover() {
        return approver;
    }

    public void setApprover(Users approver) {
        this.approver = approver;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }
}
