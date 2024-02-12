package io.crm.app.repository;


import io.crm.app.core.constant.DeleteFlag;
import io.crm.app.entity.customer.CustomerEntity;
import io.crm.app.entity.group.GroupEntity;
import io.crm.app.entity.otp.UserOtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface OtpUserRepository extends JpaRepository<UserOtpEntity, BigInteger>, JpaSpecificationExecutor<UserOtpEntity> {

    Optional<UserOtpEntity> findByUser_Id(Long id);
}
