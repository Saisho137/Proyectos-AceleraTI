package com.enyoi.arka.adapters.out.repository;

import com.enyoi.arka.adapters.out.repository.entity.OrderEntity;
import com.enyoi.arka.adapters.out.repository.entity.OrderItemEntity;
import com.enyoi.arka.domain.entities.Order;
import com.enyoi.arka.domain.entities.OrderItem;
import com.enyoi.arka.domain.entities.OrderStatus;
import com.enyoi.arka.domain.ports.out.OrderRepository;
import com.enyoi.arka.domain.valueobjects.CustomerId;
import com.enyoi.arka.domain.valueobjects.Money;
import com.enyoi.arka.domain.valueobjects.OrderId;
import com.enyoi.arka.domain.valueobjects.ProductId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.Currency;
import java.util.List;
import java.util.Optional;

public class JpaOrderRepository implements OrderRepository {
    private final EntityManager entityManager;

    public JpaOrderRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Order save(Order order) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            OrderEntity orderEntity = toEntity(order);
            entityManager.merge(orderEntity);
            transaction.commit();

            return order;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return Optional.ofNullable(
                entityManager.find(OrderEntity.class, id.value())
        ).map(this::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return entityManager.createQuery(
                        "FROM OrderEntity", OrderEntity.class
                ).getResultList()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Order> findByCustomerId(CustomerId customerId) {
        return entityManager.createQuery(
                        "FROM OrderEntity WHERE customerId = :customerId",
                        OrderEntity.class
                ).setParameter("customerId", customerId.value())
                .getResultList()
                .stream()
                .map(this::toDomain)
                .toList();
    }


    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return entityManager.createQuery(
                        "FROM OrderEntity WHERE orderStatus = :status",
                        OrderEntity.class
                ).setParameter("status", status)
                .getResultList()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Order> findPendingOrders() {
        return findByStatus(OrderStatus.PENDIENTE);
    }

    @Override
    public boolean existsById(OrderId id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(OrderId id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            OrderEntity orderEntity = entityManager.find(OrderEntity.class, id.value());
            if (orderEntity != null) {
                entityManager.remove(orderEntity);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    private OrderEntity toEntity(Order order) {
        return new OrderEntity(
                order.getId().value(),
                order.getCustomerId().value(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getItems().stream()
                        .map(orderItem ->
                                toEntity(order.getId(), orderItem))
                        .toList()
        );
    }

    private OrderItemEntity toEntity(OrderId id, OrderItem orderItem) {
        return new OrderItemEntity(
                id.value(),
                orderItem.getProductId().value(),
                orderItem.getQuantity(),
                orderItem.getUnitPrice().amount(),
                orderItem.getUnitPrice().currency().toString()
        );
    }

    private Order toDomain(OrderEntity orderEntity) {
        return Order.builder()
                .id(OrderId.of(orderEntity.getId()))
                .customerId(CustomerId.of(orderEntity.getCustomerId()))
                .createdAt(orderEntity.getCreatedAt())
                .status(orderEntity.getOrderStatus())
                .items(orderEntity.getItems().stream()
                        .map(this::toDomain)
                        .toList()
                )
                .build();
    }

    private OrderItem toDomain(OrderItemEntity orderItemEntity) {
        return OrderItem.builder()
                .productId(ProductId.of(orderItemEntity.getProductId()))
                .quantity(orderItemEntity.getQuantity())
                .unitPrice(Money.of(
                        orderItemEntity.getUnitPrice(),
                        orderItemEntity.getCurrency()
                ))
                .build();
    }
}
