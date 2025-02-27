<%@page contentType="text/html" pageEncoding="UTF-8" %>
<div class="modal fade" id="addRuleModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg" style="margin-top: 1%;">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Chọn quy tắc để thêm</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="addRuleForm">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Chọn</th>
                                <th>Tiêu đề</th>
                                <th>Mô tả</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="rule" items="${inactiveRules}">
                                <tr>
                                    <td>
                                        <input type="checkbox" class="rule-checkbox" data-rule-id="${rule.ruleID}">
                                    </td>
                                    <td><i class="fas ${rule.icon} rule-icon me-2"></i> <b>${rule.ruleTitle}</b></td>
                                    <td>
                                        <input type="text" class="form-control rule-description"
                                               data-rule-id="${rule.ruleID}" value="${rule.description}">
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="button" class="btn btn-primary" onclick="saveSelectedRules()">Lưu</button>
            </div>
        </div>
    </div>
</div>