# Demo Metric API

The API was made to be as simple as possible while allowing for flexibility in the future. The attempt was to follow the guidelines to the letter, so I stuck to three endpoints fulfilling the three required actions. My initial thought was to create a restful endpoint for the Metric entity and then provide a summary action, but I think for ease of use, providing a simple add metric entry method is better and is less tightly coupled to the internals of the metric entity if it were to change in the future. Based on your desire for a developer with spring IOC and boot experience, I figured I’d showcase those in this project as well.

### MetricApi Endpoint

The endpoint receives the data and passes it along to the controller handling expected exceptions. The API Documentation has more details on the endpoints.

### MetricController

The controller injected into the endpoint decides how to handle the actions and uses a store to persist required data. The controller also takes in an application property for the type of optimization to use for the metric actions. The Metric section explains the optimization differences.

### MetricStore

The store injected into the controller provides the contract for persisting and retrieving the data. I also decided that the persistence layer would provide the unique id for the Metric entities. The store was implemented with an in-memory store, as a basic map from id to Metric, but could be swapped out for something else, such as H2 or MongoDB store.

### Metric

Basic entity that is extended by ReadOptimizedMetric. I created special entity because while the requirements for this were to optimize the reads, metrics are more commonly optimized for writes, because, often, metrics are added far more frequently than read/checked.

### MetricSummary

The summary has specific requirements to calculate for the metric in a read optimized way but I chose to write it in a way where it could calculate it efficiently for either the read or write optimized version of the metric entity. I also chose to separate the summary from the metric for two reasons. First, it’s possible the user would not want a summary after every single metric call (updates provide the updated metric to the user). Second, this provides the option for creating different metric summaries that could be configured into the system.

### Optimizing Reads

#### Mean

The best way to optimize reading the mean is to keep a running sum of the MetricEntry values to avoid the O(n) work of adding up all entries. By doing this mean becomes an O(1) operation. This has a space cost in our in memory store for a single double value.

#### Median

The median requires getting the middle element or middle two elements of the value ordered metric entries. To optimize this for reading, the array of entries is kept sorted by value allowing the median to be calculated in O(1). The API therefore does not guarantee ordering of the metrics entries, but I chose to also include an order property for if they need to be looked at in entry added order.

#### Min/Max

Min and Max also utilized the sorted order of the array to be calculated in O(1).

Note: MetricSummary was written so that it could use read or write optimized Metrics and still calculate the most efficient way possible. I used two methods, getKthGreatestEntryValue(k) and getKthSmallestEntryValue(k) for getting the value ordered elements in summary. This way the getting of those elements is based on the Metric. For the read optimized metric it’s a simple operation since it’s sorted. For a write optimized implementation of metric, finding the kth smallest or largest element could be done with QuickSelect implementation, which has a worst case O(n^2) but an average case of O(n). Sorting would be another option that would result in a consistent O(nlogn).

### Known Limitation

The only way to guarantee consistency for adding values to the metrics in a multithread environment was to synchronize the addMetrics function in the controller, slowing writes even further. Otherwise adding two metric values simultaneously, could cause the stores’ update to overwrite the previous change. This could be solved if the entries were a relational entity instead of embedded but that does have read time considerations.

The total of all entries is limited to the size of a Max double due to the running total and mean calculation. This is a tradeoff for write speeds. You could calculate mean in a way to avoid overflows but there’d be more space and time tradeoffs and it would complicate this function greatly so it’d need to be specified whether the customers use case required the considering.

The store has references to object that exist outside the store so modifications outside of the store are possible. This would be solved by having a better store system. As it is this store system is more of a POC test store.

# API Documentation

## Endpoints
| Action             | Path            | Type | Parameters | Return with Example | Description                                                                                                   |  
|--------------------|-----------------|------|------------|---------------------|---------------------------------------------------------------------------------------------------------------|
| Create Metric      | /CreateMetric   | PUT  |  **name**=String Name of the metric.        | JSON object of Metric type <br> `{"metricName":"TestMetric2","metricId":0, "metricEntries":[]}`                 | Creates a metric item and persists the item to the store, returning   the resulting Metric item type as JSON. | 
| Add Metric Entry   | /AddMetricEntry | POST |**metricId**=Long ID property of the metric <br> **value**=Float Float value for adding to metric’s entries     | JSON object of Metric type <br>         `{"metricName":"TestMetric2", "metricId":0, "metricEntries":[{"metricValue":6.5,"order":0}]}` <br> Possible 404 if metricId not found.          | Adds a MetricEntry with the value provided to an existing Metric with   the associated metricId.              |
| Get Metric Summary | /MetricSummary  | PUT  |      **metricId**=Long ID property of the metric      |   JSON object of Metric Summary type <br>      `{ "metricName": "TestMetric2", "mean": 5.09689998626709, "median": 5.25, "min": 1, "max": 8.8876, "metricId": 0}`<br> Possible 404 if metricId not found.<br> Not all properties will be available if there are not MetricEntries | Gets the summary for the metric with the associated id.                                                       |

Note all endpoints will return 400 on illegal arguments. 

### Endpoint Time and Space
| Endpoint | Time | Space |
|--|--|--|
| Create Metric | O(1) <br> There's not much happening here to increase complexity. |  O(1) <br> This creates 1 Metric entry and only one copy of each piece of the data, except for the running sum, resulting in 64 bits(one double) of duplicate information. |
| Add Metric Entry | O(n) <br> This is bound by the time that it takes to insert the new entry into the correct value ordered slot in the metric's entries | O(n) <br> This is really for the general storage of all the metric entries. Each entry is stored once.
| Add Metric Entry | O(1) <br> Each operation in the summary is calculated in O(1) because of the ReadOptimizedMetric | O(1) None of this information in this endpoint is stored and even for the short life of the summary object the memory does not increase for each entry.

Note: All of the assume that the store access time is O(1) which it is for the map used in the InMemoryStore(amoritized). 

# Build and Run information
This is based in spring boot with maven so running is super easy. 

    git clone this-repos-clone-url
    ./mvnw spring-boot:run

To build and run unit tests

    ./mvnw deploy

## Quick API call examples

    # Create metric with name TestMetric2
    curl -X PUT 'http://localhost:8080/CreateMetric?name=TestMetric2'
    # Get metric with metric id 0
    curl -X GET 'http://localhost:8080/MetricSummary?metricId=0'
    #Add Metric entry to metric id 0 with value 8.5
    curl -X POST http://localhost:8080/AddMetricEntry -d 'metricId=0&value=8.5'
