
# Demo Metric API

The API was made to be as simple as possible while allowing for flexibility in the future. The attempt was to follow the guidelines to the letter, so I stuck to three endpoints fulfilling the three required actions. My initial thought was to create a restful endpoint for the Metric entity and then provide a summary action, but I think for ease of use, providing a simple add metric entry method is better and is less tightly coupled to the internals of the metric entity if it were to change in the future. Based on your desire for a developer with spring IOC and boot experience, I figured I’d showcase those in this project as well.

### MetricApi Endpoint

The endpoint receives the data and passes it along to the metric service handling expected exceptions using the RestReponseEntityExceptionHandler. The API Documentation has more details on the endpoints.

### MetricController

The service injected into the endpoint decides how to handle the actions and uses a store to persist required data. 

### MetricStore

The store injected into the metric service provides the contract for persisting and retrieving the data. The store was implemented with an in-memory store, as a basic map from name to Metric, but could be swapped out for something else, such as H2 or MongoDB store.

### Metric

Basic entity that is extended by ReadOptimizedMetric. I created special entity because while the requirements for this were to optimize the reads, metrics are more commonly optimized for writes because, often, metrics are added far more frequently than read/checked.

### MetricSummary

Metric Summary defers the calculation of min/max/median to the metric, but does the calculation for mean.

### Optimizing Reads

#### Mean

The best way to optimize reading the mean is to keep a running sum of the MetricEntry values to avoid the O(n) work of adding up all entries. By doing this mean becomes an O(1) operation. This has a space cost in our in memory store for a single double value.

#### Median

The median uses to heaps to calculate a rolling median. The lower half values are kept in a max heap and the upper half values are kept in a min heap so that the median value(s) are always O(1) access.

#### Min/Max
The min and max are checked against any new values for whether they should be updated, making setting and getting them O(1).

### Known Limitation

The only way to guarantee consistency for adding values to the metrics in a multithread environment was to synchronize the store's add metric entry method. Otherwise adding two metric values simultaneously, could cause the stores’ update to overwrite the previous change. A better store could either have relational tables for the update or for MongoDb you could use one of the document push/update methods to handle the situation.

The total of all entries is limited to the size of a Max double due to the running total and mean calculation. This is a tradeoff for write speeds. You could calculate mean in a way to avoid overflows but there’d be more space and time tradeoffs and it would complicate this function greatly so it’d need to be specified whether the customers use case required that consideration.

The store has references to object that exist outside the store so modifications outside of the store are possible. This would be solved by having a better store system that provided new references on changes. 

# API Documentation

## Endpoints
| Action             | Path            | Type | Parameters | Return with Example | Description                                                                                                   |  
|--------------------|-----------------|------|------------|---------------------|---------------------------------------------------------------------------------------------------------------|
| Create Metric      | /Metric   | POST  |  **name**=String Name of the metric.        | JSON object of Metric type <br> `{"metricName":"TestMetric2","metricEntries":[]}`                 | Creates a metric item and persists the item to the store, returning   the resulting Metric item type as JSON. | 
| Add Metric Entry   | /MetricEntry | POST |**metricName**=String name of the metric <br> **value**=Float Float value for adding to metric’s entries     | JSON object of Metric type <br>         `{"metricName":"TestMetric2",  "metricEntries":[{"metricValue":6.5}]}` <br> Possible 404 if metricName is not found.          | Adds a MetricEntry with the value provided to an existing Metric with   the associated metricName.              |
| Get Metric Summary | /MetricSummary  | GET  |      **metricName**=String name of the metric      |   JSON object of Metric Summary type <br>      `{ "metricName": "TestMetric2", "mean": 5.09689998626709, "median": 5.25, "min": 1, "max": 8.8876}`<br> Possible 404 if metricName not found.<br> Not all properties will be available if there are not MetricEntries | Gets the summary for the metric with the associated name.                                                       |

Note all endpoints will return 400 on illegal arguments. 

### Endpoint Time and Space
| Endpoint | Time | Space |
|--|--|--|
| Create Metric | O(1)  |  O(1) |
| Add Metric Entry | O(logn) <br> This is bound by the time to insert into the median tracking heaps. Offering to the heaps is log(n). | O(n) <br> This is really for the general storage of all the metric entries. Each entry is stored once.
| Get Metric Summary | O(1) <br> Each operation in the summary is calculated in O(1) because of the ReadOptimizedMetric | O(1) 

Note: All of the assume that the store access time is O(1) which it is for the map used in the InMemoryStore(amoritized). 

# Build and Run information
This is based in spring boot with maven so running is super easy. 

    git clone this-repos-clone-url
    # For unix
	./mvnw spring-boot:run
	# For Windows cmd
	.\mvnw.cmd spring-boot:run
	#To change the ports
	./mvnw spring-boot:run -Dserver.port=8090

To build and run unit tests

    ./mvnw package spring-boot:repackage

## Quick API Call Examples

    # Create metric with name TestMetric2
    curl -X POST 'http://localhost:8080/Metric?metricName=TestMetric2'
    # Get metric with metric name TestMetric2
    curl -X GET 'http://localhost:8080/MetricSummary?metricName=TestMetric2'
    #Add Metric entry to metric name TestMetric2 with value 8.5
    curl -X POST http://localhost:8080/MetricEntry -d 'metricName=0&value=8.5'
