package com.demo.backend.voucher.repository;

import com.demo.backend.voucher.model.UserVoucher;
import com.demo.backend.voucher.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {

    long countByUserIdAndVoucher(Long userId, Voucher voucher);

    // Fetch all usage by user
    List<UserVoucher> findByUserId(Long userId);

    // Fetch all usage for one voucher
    List<UserVoucher> findByVoucher(Voucher voucher);

    // ⭐ Fetch all voucher usage with JOIN FETCH
    @Query("""
            SELECT uv FROM UserVoucher uv
            JOIN FETCH uv.voucher v
            ORDER BY uv.usedAt DESC
            """)
    List<UserVoucher> findAllWithVoucher();

    // ⭐ Fetch usage history for a specific user
    @Query("""
            SELECT uv FROM UserVoucher uv
            JOIN FETCH uv.voucher v
            WHERE uv.userId = :userId
            ORDER BY uv.usedAt DESC
            """)
    List<UserVoucher> findHistoryByUserId(Long userId);

}
