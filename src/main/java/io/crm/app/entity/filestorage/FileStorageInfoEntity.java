package io.crm.app.entity.filestorage;

import io.crm.app.entity.audit.AbstractAuditableEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "file_info")
public class FileStorageInfoEntity extends AbstractAuditableEntity {
    private static final long serialVersionUID = -1499572960307534233L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_info_id", nullable = false)
    private BigInteger id;

    @Column(name = "file_name", length = 100)
    private String fileName;

    @Column(name = "file_description", length = 200)
    private String fileDescription;

    @Column(name = "file_path", length = 400)
    private String filePath;

    @Column(name = "storage_file_name", length = 1000)
    private String storagefileName;

}