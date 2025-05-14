# MartClinic

MartClinic is a modern Android application built with Jetpack Compose, following clean architecture principles and utilizing the latest Android development technologies. The app provides a user-friendly interface for managing clinic data with real-time API communication.

## 🚀 Technologies & Libraries

- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern Android UI toolkit
- **Material 3** - Design system for beautiful and consistent UI
- **Dagger Hilt** - Dependency injection framework
- **Retrofit** - Type-safe HTTP client for API communication
- **Moshi** - Modern JSON library for Kotlin
- **OkHttp** - HTTP client with logging capabilities
- **Coroutines** - Asynchronous programming
- **ViewModel** - UI-related data holder
- **Navigation Compose** - Navigation component for Jetpack Compose

## 📱 Features

- Modern Material 3 UI design
- Clean Architecture implementation
- Dependency Injection with Hilt
- Network layer with Retrofit and OkHttp
- JSON parsing with Moshi
- Asynchronous operations with Coroutines
- Navigation with Jetpack Navigation Compose
- Real-time API communication
- Pagination support
- Error handling and retry mechanisms

## 🏗️ Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com.example.martclinic/
│   │   │   │   ├── data/           # Data layer
│   │   │   │   │   ├── cache/      # Caching implementation
│   │   │   │   │   ├── model/      # Data models
│   │   │   │   │   ├── remote/     # API service interfaces
│   │   │   │   │   └── repository/ # Repository implementations
│   │   │   │   ├── di/             # Dependency injection modules
│   │   │   │   ├── ui/             # UI components
│   │   │   │   │   ├── screens/    # Screen composables
│   │   │   │   │   ├── theme/      # Theme configuration
│   │   │   │   │   └── viewmodel/  # ViewModel classes
│   │   │   │   └── utils/          # Utility classes
│   │   ├── res/                    # Resources
│   │   └── AndroidManifest.xml
│   └── test/                       # Unit tests
```

## 🌐 Network Configuration

The app supports different network configurations for development and testing:

### Development Environments

1. **Wired Connection (Emulator)**
   - Use `http://10.0.2.2:3000/api/` for Android emulator
   - This IP address is the host machine's localhost from the emulator's perspective

2. **Wireless Connection (Physical Device)**
   - Use your machine's local IP address (e.g., `http://192.168.219.104:3000/api/`)
   - Both device and server must be on the same network
   - Find your IP address using `ipconfig` (Windows) or `ifconfig` (macOS/Linux)

### Configuration Steps

1. Update the `BASE_URL` in `NetworkModule.kt`:
   ```kotlin
   // For emulator
   private const val BASE_URL = "http://10.0.2.2:3000/api/"
   
   // For physical device
   private const val BASE_URL = "http://YOUR_IP_ADDRESS:3000/api/"
   ```

2. Ensure network security configuration in `network_security_config.xml` includes your IP:
   ```xml
   <domain-config cleartextTrafficPermitted="true">
       <domain includeSubdomains="true">localhost</domain>
       <domain includeSubdomains="true">10.0.2.2</domain>
       <domain includeSubdomains="true">YOUR_IP_ADDRESS</domain>
   </domain-config>
   ```

## 🛠️ Development Setup

1. **Prerequisites**
   - Android Studio Hedgehog | 2023.1.1 or later
   - Kotlin 1.9.0 or later
   - Gradle 8.0 or later
   - Minimum SDK: 24
   - Target SDK: 35

2. **Setup Steps**
   - Clone the repository
   - Open the project in Android Studio
   - Sync the project with Gradle files
   - Configure the network settings based on your development environment
   - Build and run the application

3. **Running the App**
   - For emulator testing: Use the wired configuration
   - For physical device testing: Use the wireless configuration
   - Ensure the backend server is running and accessible

## 🔧 Dependencies

The project uses the following major dependencies:
- AndroidX Core KTX
- AndroidX Lifecycle
- AndroidX Compose
- Material 3
- Retrofit 2.9.0
- OkHttp 4.12.0
- Moshi 1.15.0
- Coroutines 1.8.0
- Dagger Hilt 2.50

## 🐛 Troubleshooting

### Common Issues

1. **Connection Timeout**
   - Verify server is running
   - Check network connectivity
   - Ensure correct IP address configuration
   - Check firewall settings

2. **Cleartext Traffic Error**
   - Update `network_security_config.xml`
   - Add your IP to the allowed domains

3. **API Communication Issues**
   - Check server logs
   - Verify API endpoints
   - Ensure correct port configuration

## 📄 License

This project is licensed under the terms of the MIT license. 