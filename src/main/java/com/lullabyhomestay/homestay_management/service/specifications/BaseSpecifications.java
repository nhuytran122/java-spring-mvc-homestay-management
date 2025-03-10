package com.lullabyhomestay.homestay_management.service.specifications;

import org.springframework.data.jpa.domain.Specification;

public class BaseSpecifications {
    public static <T> Specification<T> like(String field, String value) {
        return (root, query, cb) -> value == null || value.trim().isEmpty() ? cb.conjunction()
                : cb.like(cb.lower(root.get(field)), "%" + value.trim().toLowerCase() + "%");
    }

    public static <T> Specification<T> equal(String field, Object value) {
        return (root, query, cb) -> value == null ? cb.conjunction() : cb.equal(root.get(field), value);
    }

    public static <T> Specification<T> equalJoin(String joinField, String subField, Long valueID) {
        return (root, query, cb) -> valueID == null ? cb.conjunction()
                : cb.equal(root.get(joinField).get(subField), valueID);
    }

    public static <T> Specification<T> likeJoin(String joinField, String subField, String value) {
        if (value == null || value.trim().isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        String normalizedValue = value.trim().toLowerCase();
        String searchPattern = "%" + normalizedValue + "%";
        return (root, query, cb) -> cb.like(cb.lower(root.get(joinField).get(subField)), searchPattern);
    }

    public static <T> Specification<T> equalJoinTwoLevels(String firstJoinField, String secondJoinField,
            String finalField, Long valueID) {
        return (root, query, cb) -> valueID == null ? cb.conjunction()
                : cb.equal(root.get(firstJoinField).get(secondJoinField).get(finalField), valueID);
    }

    public static <T> Specification<T> likeJoinTwoLevels(String firstJoinField, String secondJoinField,
            String finalField, String value) {
        if (value == null || value.trim().isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        String normalizedValue = value.trim().toLowerCase();
        String searchPattern = "%" + normalizedValue + "%";
        return (root, query, cb) -> cb.like(root.get(firstJoinField).get(secondJoinField).get(finalField),
                searchPattern);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Specification<T> between(String field, Object firstValue, Object secondObject) {
        if (firstValue == null || secondObject == null) {
            return (root, query, cb) -> cb.conjunction();
        }
        return (root, query, cb) -> cb.between(root.get(field), (Comparable) firstValue, (Comparable) secondObject);
    }

}