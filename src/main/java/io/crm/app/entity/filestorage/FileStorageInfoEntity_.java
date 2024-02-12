package io.crm.app.entity.filestorage;

import io.crm.app.entity.audit.AbstractAuditableEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigInteger;

@StaticMetamodel(FileStorageInfoEntity.class)
public class FileStorageInfoEntity_ extends AbstractAuditableEntity_ {

    public static volatile SingularAttribute<FileStorageInfoEntity, BigInteger> id;

    public static volatile SingularAttribute<FileStorageInfoEntity, String> fileName;

    public static volatile SingularAttribute<FileStorageInfoEntity, String> fileDescription;
    public static volatile SingularAttribute<FileStorageInfoEntity, String> filePath;

    public static volatile SingularAttribute<FileStorageInfoEntity, String> storagefileName;

}