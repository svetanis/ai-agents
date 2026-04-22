#User:
Given an array of points where points[i] = [xi, yi] 
represents a point on the X-Y plane and an integer k, 
return the k closest points to the origin (0, 0).

The distance between two points on the X-Y plane is 
the Euclidean distance (i.e., √(x1 - x2)2 + (y1 - y2)2).

You may return the answer in any order. 
The answer is guaranteed to be unique 
(except for the order that it is in).

Constraints:
1 <= k <= points.length <= 104
-104 <= xi, yi <= 104

RESPONSE:

JAVA 
```
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Solution {

    // Helper record to store the squared distance along with the point.
    // Using a record provides a concise way to define immutable data carriers.
    private record PointWithDistance(long squaredDistance, int[] point) {}

    public int[][] kClosest(int[][] points, int k) {
        // Calculate squared Euclidean distance for each point and store with the point.
        // Using squared distance avoids expensive sqrt calculations, as the relative order remains the same.
        List<PointWithDistance> distancesWithPoints = new ArrayList<>();
        for (int[] point : points) {
            long x = point[0];
            long y = point[1];
            long squaredDistance = x * x + y * y;
            distancesWithPoints.add(new PointWithDistance(squaredDistance, point));
        }

        // Sort the points based on their squared distances.
        // Using Comparator.comparingLong for efficient comparison of long values.
        distancesWithPoints.sort(Comparator.comparingLong(PointWithDistance::squaredDistance));

        // Extract the k closest points.
        int[][] result = new int[k][2];
        for (int i = 0; i < k; i++) {
            result[i] = distancesWithPoints.get(i).point();
        }

        return result;
    }
}
```

PYTHON
```
class Solution:
    def kClosest(self, points: list[list[int]], k: int) -> list[list[int]]:
        # Calculate squared Euclidean distance for each point and store with the point.
        # Using squared distance avoids expensive sqrt calculations, as the relative order remains the same.
        # Refactored to use a list comprehension for conciseness.
        distances_with_points = [(x*x + y*y, [x, y]) for x, y in points]

        # Sort the points based on their squared distances.
        distances_with_points.sort(key=lambda item: item[0])

        # Extract the k closest points.
        # Refactored to use list slicing and a list comprehension for conciseness.
        return [point for _, point in distances_with_points[:k]]
```
