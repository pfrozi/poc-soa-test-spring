package poc.rest.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import poc.rest.test.item.ItemRepository;

import com.mongodb.MongoClientURI;

@Configuration
@EnableMongoRepositories(basePackages="poc.rest.test" )
public class MongoDBConfig {
    @Autowired
    ItemRepository itemRepository;  
    @Bean
    public  MongoDbFactory mongoDbFactory() throws Exception {

        return new SimpleMongoDbFactory(new MongoClientURI("mongodb://root:r00t@ds133249.mlab.com:33249/poc-soa-test-db"));
    }
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }
}  