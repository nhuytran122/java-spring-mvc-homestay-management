<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="welcome-banner">
    <div class="container pt-5">
        <div class="row align-items-center">
            <div class="col-md-6">
                <h1>Xin chào, ${customer.fullName}!</h1>
                <p>Hãy để Lullaby trở thành người bạn đồng hành trong kỳ nghỉ của bạn!</p>
                <div class="stats-container mt-4">
                    <div class="row g-3">
                        <div class="col-6 col-md-5">
                            <div class="stat-card">
                                <h3><fmt:formatNumber type="number" value="${customer.rewardPoints}" /></h3>
                                <p>Điểm tích lũy</p>
                            </div>
                        </div>
                        <div class="col-6 col-md-7">
                            <div class="stat-card">
                                <h3>${customer.customerType.name}</h3>
                                <p>Hạng thành viên</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6 text-end">
                <img src="https://images.unsplash.com/photo-1566073771259-6a8506099945" alt="Homestay" class="welcome-image">
            </div>
        </div>
    </div>
</div>