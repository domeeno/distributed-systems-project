package com.pandora.courseservice.repository

import com.pandora.courseservice.models.Topic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.GraphLookupOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Repository

@Repository
class GraphLookupRepository(
    @Autowired val mongoTemplate: MongoTemplate
) {

    fun getTopicTree(topicId: String, maxDepth: Long = 16L): Topic {
        val criteria = Criteria("_id").`is`(topicId)

        val matchStage = Aggregation.match(criteria)

        val operation = GraphLookupOperation.builder()
            .from("topics")
            .startWith("\$_id")
            .connectFrom("_id")
            .connectTo("parent_id")
            .maxDepth(maxDepth)
            .`as`("all_topics")

        val aggregation = Aggregation.newAggregation(matchStage, operation)
        val parentTopic = mongoTemplate.aggregate(aggregation, "topics", Topic::class.java).mappedResults.first()

        // Nesting the topicTree
        // once for parentTopic
        parentTopic.childTopics = parentTopic.allTopics.filter { it.id in parentTopic.childIds }
        parentTopic.allTopics = parentTopic.allTopics.filter { it.id !in parentTopic.childIds }

        // now recursion magic - this travels through children topics and nests the tree properly
        nestTree(parentTopic.childTopics, parentTopic.allTopics)

        // empty all topics for energy efficiency
        parentTopic.allTopics = emptyList()

        return parentTopic
    }

    private fun nestTree(topicTree: List<Topic>, allTopics: List<Topic>) {
         for(topic in topicTree) {
             topic.childTopics = allTopics.filter { it.id in topic.childIds }

             if (!(topic.childTopics.none { it.childTopics.isEmpty() })) {
                nestTree(topic.childTopics, allTopics)
             }
         }
    }
}
