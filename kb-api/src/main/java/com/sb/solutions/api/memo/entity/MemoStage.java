package com.sb.solutions.api.memo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

import com.sb.solutions.api.memo.enums.Stage;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.enitity.BaseEntity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemoStage extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @Exclude
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @OneToOne
    @NotNull
    @JoinColumn(name = "sent_by")
    private User sentBy;

    @OneToOne
    @NotNull
    @JoinColumn(name = "sent_to")
    private User sentTo;

    @NotNull
    @Column(name = "stage", nullable = false)
    private Stage stage;

    @Column(name = "note", nullable = false)
    @NotNull
    private String note;
}
