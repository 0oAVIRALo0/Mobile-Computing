## Distance Manager

The `DistanceManager` class is responsible for managing distances in kilometers and miles. It initializes distance maps, calculates total distances, and retrieves distances based on the stop index and unit type.

### Initialization

The distance maps are initialized with random distances using the `randomNum()` function.

### Unit Conversion

Distances in kilometers are converted to miles using the `convertKilometersToMiles()` function.

## Distance Manager Lazy

The `DistanceManagerLazy` class implements lazy loading for distance maps. It generates distances on-demand and allows retrieval of distances based on the stop index and unit type.

### Lazy Initialization

Distance maps are generated lazily using the `by lazy` delegate, ensuring that they are only created when accessed.

### Unit Conversion

Similar to `DistanceManager`, distances in kilometers are converted to miles.

## Progress Manager

The `ProgressManager` class handles updating progress bars and building progress text for the user interface.

### Progress Bar Update

The `updateProgressBar` function animates the progress bar based on the current, cumulative, and total distances.

### Progress Text Building

The `buildProgressText` function constructs a text summary of the current progress, including the current stop index, total distance, distance covered, and remaining distance.

## MainActivity

The `MainActivity` class is the main activity of the Android application. It initializes UI components, handles button clicks, and interacts with the `DistanceManager`, `DistanceManagerLazy`, and `ProgressManager` classes.

### UI Components

- `progressTextView`: Displays progress text.
- `progressBar`: Visual representation of progress.
- `switchUnitsButton`: Button to switch between kilometers and miles.
- `reachedNextStopButton`: Button to simulate reaching the next stop.
- `lazyTextView`: Lazy-loaded text view.
- `showLazyTextButton`: Button to show/hide lazy text.
- `showNormalTextButton`: Button to show/hide normal text.
- `showNormalListButton`: Button to show a list of normal distances.
- `showLazyListButton`: Button to show a list of lazy-loaded distances.
- `showLists`: Text view to display lists of distances.

### Initialization

- `distanceManager`: Instance of `DistanceManager`.
- `progressManager`: Instance of `ProgressManager`.
- `distanceManagerLazy`: Instance of `DistanceManagerLazy`.

### Switching Units

Buttons and logic for switching units between kilometers and miles are implemented.

### Button Clicks

Handlers for button clicks to simulate progress, show/hide text, and display distance lists.

### Example Usage

1. Launch the app.
2. Click `Show Normal Text`.
3. Click `Reached Next Stop` to simulate progress.
4. Optionally, click `Switch to Miles`.
5. Explore other functionalities such as lazy-loaded text, distance lists, and unit switching.

## Notes

- Distances are randomly generated for demonstration purposes.
- The application assumes a linear route with 10 stops.

![Screenshot 2024-02-19 at 8 27 53 PM](https://github.com/0oAVIRALo0/Mobile-Computing/assets/99357517/3768cb03-c1f7-47e1-ae1b-8e2ecb0531fe)
![Screenshot 2024-02-19 at 8 28 28 PM](https://github.com/0oAVIRALo0/Mobile-Computing/assets/99357517/68325c22-df1b-4511-93a9-6797f17bb568)
![Screenshot 2024-02-19 at 8 28 49 PM](https://github.com/0oAVIRALo0/Mobile-Computing/assets/99357517/e49d7897-7800-4447-ae8e-ddf21f25b7f0)
![Screenshot 2024-02-19 at 8 29 11 PM](https://github.com/0oAVIRALo0/Mobile-Computing/assets/99357517/e8bfaf38-ccef-49d9-b621-2a0f0d58507e)


