# Face Compare Library
<img src="https://views.whatilearened.today/views/github/dikamahard/FaceCompareLibrary.svg" alt="View" />   

[![](https://jitpack.io/v/dikamahard/FaceCompareLibrary/month.svg)](https://jitpack.io/#dikamahard/FaceCompareLibrary)   

an Andorid library that provide a simple API to compare the similarity between 2 faces from bitmap. This can be implemented to a face recognition system, face authentication, face matching, and many more that needed face comparison technique. This library implemented Google MLKit to detect faces and MobileFaceNet CNN model to compare faces

## Implement this to your project
In your project level `setting.gradle` file, add the following:
 
`Groovy DSL syntax`
```gradle
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' } // Must be added
		}
	}
```
`Kotlin DSL syntax`
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // Must be added
    }
}
```
In your app level `build.gradle` file, add the following dependency:

`Groovy DSL syntax`
```gradle
dependencies {
	    implementation 'com.github.dikamahard:FaceCompareLibrary:1.0.2'
}
```
`Koltin DSL syntax`
```gradle
dependencies {
	    implementation ("com.github.dikamahard:FaceCompareLibrary:1.0.2")
}
```

## Implement this library locally

1. git clone https://github.com/dikamahard/FaceCompareLibrary
2. open your project in android studio
3. click file -> new -> import module
4. find the path of the face compare library that you cloned
5. implement the dependencies in your app level build.gradle  

## Usage
```kotlin
val fc = FaceCompare(assets)

fc.compareFaces(bitmap1, bitmap2) { result ->

    if (result) {
        // Faces match
        // Do something
        Log.d("TAG", "FACES MATCH")

    } else {
        // Faces don't match
        // Do something else
        Log.d("TAG", "FACE DO NOT MATCH")
    }

}
```
## Contributing
I know my code is messy, ineffective, and spaghetti, so feel free to make a pull requests to fix my messy code

## Reference
[MobileFaceNets: Efficient CNNs for Accurate Real-Time Face Verification on Mobile Devices](https://arxiv.org/abs/1804.07573)

[MobileFaceNet TensorFlow](https://github.com/sirius-ai/MobileFaceNet_TF)

[MobileFaceNet Implementation](https://github.com/SphericalKat/face-verify-MFN)

[Google ML Kit](https://developers.google.com/ml-kit)
