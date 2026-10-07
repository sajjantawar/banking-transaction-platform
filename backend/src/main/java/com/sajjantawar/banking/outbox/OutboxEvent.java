package com.sajjantawar.banking.outbox;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity
@Table(name="outbox_events", indexes=@Index(name="idx_outbox_pending", columnList="published, created_at"))
public class OutboxEvent {
 @Id private UUID id; @Column(name="aggregate_id",nullable=false) private UUID aggregateId;
 @Column(nullable=false,length=120) private String eventType; @Column(nullable=false,columnDefinition="TEXT") private String payload;
 @Column(nullable=false) private boolean published; @Column(name="created_at",nullable=false) private Instant createdAt; @Column(name="published_at") private Instant publishedAt;
 protected OutboxEvent(){}
 public OutboxEvent(UUID id,UUID aggregateId,String eventType,String payload){this.id=id;this.aggregateId=aggregateId;this.eventType=eventType;this.payload=payload;this.createdAt=Instant.now();}
 public UUID getId(){return id;} public UUID getAggregateId(){return aggregateId;} public String getEventType(){return eventType;} public String getPayload(){return payload;} public boolean isPublished(){return published;} public Instant getCreatedAt(){return createdAt;} public Instant getPublishedAt(){return publishedAt;}
 public void markPublished(){published=true;publishedAt=Instant.now();}
}