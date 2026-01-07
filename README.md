# Video-Channel-Platform

A Java-based video channel platform that simulates a content distribution system with channels, followers, and video recommendations. This platform models the relationship between content creators (channels) and their audience (subscribers and monitors), including video releases, recommendations, and watch statistics.

## Features

### Core Components

- **Channels**: Content creators that can release videos and manage followers
- **Subscribers**: Users who follow channels and receive personalized video recommendations
- **Monitors**: Analytics tools that track viewing statistics for channels

### Key Functionality

- **Channel Management**: Create channels with configurable limits for followers and videos
- **Follow/Unfollow System**: Users can follow and unfollow channels dynamically
- **Video Release & Recommendations**: Channels release videos that are automatically recommended to subscribers
- **Watch Statistics**: Monitors track viewing metrics including:
  - Number of views
  - Maximum watch time
  - Average watch time per channel

### Exception Handling

The platform includes robust error handling for:
- Duplicate video releases (`VideoAlreadyReleasedException`)
- Duplicate follow attempts (`AlreadyFollowedException`)
- Invalid unfollow operations (`FollowerNotFoundException`)
- Watching non-recommended videos (`VideoNotRecommendedException`)

## Project Structure

```
Video_Channel_Platform/
├── src/
│   ├── model/
│   │   ├── Channel.java                    # Channel implementation
│   │   ├── Follower.java                   # Base follower class
│   │   ├── Subscriber.java                 # Subscriber implementation
│   │   ├── Monitor.java                    # Monitor implementation
│   │   ├── AlreadyFollowedException.java   # Exception classes
│   │   ├── FollowerNotFoundException.java
│   │   ├── VideoAlreadyReleasedException.java
│   │   └── VideoNotRecommendedException.java
│   └── junit_tests/
│       └── StarterTests.java               # JUnit test suite
└── bin/                                     # Compiled classes
```

## Building and Running

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- JUnit 4 for running tests (e.g., junit-4.13.2.jar)
- Hamcrest Core for JUnit dependencies (e.g., hamcrest-core-1.3.jar)

**Note**: Download JUnit and Hamcrest JARs from [Maven Central](https://search.maven.org/) or use a build tool like Maven/Gradle. Replace `junit-4.x.jar` and `hamcrest-core-1.x.jar` in the commands below with your actual JAR filenames.

### Compilation

From the project root directory (parent of Video_Channel_Platform):

```bash
# Compile all source files
javac -d Video_Channel_Platform/bin Video_Channel_Platform/src/model/*.java

# Compile with JUnit tests (requires JUnit and Hamcrest JARs in classpath)
javac -cp Video_Channel_Platform/bin:junit-4.x.jar:hamcrest-core-1.x.jar -d Video_Channel_Platform/bin Video_Channel_Platform/src/model/*.java Video_Channel_Platform/src/junit_tests/*.java
```

### Running Tests

```bash
# Run JUnit tests
java -cp Video_Channel_Platform/bin:junit-4.x.jar:hamcrest-core-1.x.jar org.junit.runner.JUnitCore junit_tests.StarterTests
```

## Usage Examples

### Creating a Channel

```java
// Create a channel with max 50 followers and max 100 videos
Channel channel = new Channel("Cafe Music BGM", 50, 100);
```

### Creating Followers

```java
// Create a subscriber with max 20 channels and max 40 recommended videos
Subscriber subscriber = new Subscriber("Alan", 20, 40);

// Create a monitor with max 20 channels
Monitor monitor = new Monitor("Stat Sensor A", 20);
```

### Following a Channel

```java
try {
    channel.follow(subscriber);
    // Both channel and subscriber are updated
} catch (AlreadyFollowedException e) {
    System.out.println("Already following this channel");
}
```

### Releasing Videos

```java
try {
    channel.releaseANewVideo("Monday Jazz");
    // Video is automatically recommended to all subscribers
} catch (VideoAlreadyReleasedException e) {
    System.out.println("Video already released");
}
```

### Watching Videos and Tracking Statistics

```java
try {
    // Subscriber watches a recommended video
    subscriber.watch("Monday Jazz", 25); // 25 minutes watch time
    
    // Monitor statistics are automatically updated for all channel monitors
} catch (VideoNotRecommendedException e) {
    System.out.println("Video not recommended to this subscriber");
}
```

### Unfollowing a Channel

```java
try {
    channel.unfollow(subscriber);
    // Both channel and subscriber are updated
} catch (FollowerNotFoundException e) {
    System.out.println("Follower not found");
}
```

## Design Principles

### Bidirectional Updates
When a follower follows or unfollows a channel, both objects are updated automatically to maintain consistency.

### Automatic Recommendations
Videos released by a channel are immediately recommended to all current subscribers (not monitors).

### Time-based Analytics
Monitors only track statistics for videos watched after they start following a channel, ensuring accurate temporal analytics.

### Unique Constraints
- Video names are unique across all channels
- A follower cannot follow the same channel twice
- A channel cannot release the same video twice

## Testing

The project includes comprehensive JUnit tests covering:
- Channel creation and management
- Follower operations (follow/unfollow)
- Video release and recommendations
- Watch statistics tracking
- Exception handling for edge cases

Run the complete test suite using the commands in the "Running Tests" section above.

## License

This project is part of an educational exercise and is provided as-is for learning purposes.