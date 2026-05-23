package dev.dercommander.sels;


import org.lwjgl.sdl.*;

public class Main {
    static void main(String[] args) throws InterruptedException {
        System.out.println("Initializing SuperEarlyLoadingScreen...");

        PlatformParameters platformParams;

        try {
            platformParams = PlatformParameters.parseFromArgs(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Init failed: " + e);
            return;
        }

        int globalProps = SDLProperties.SDL_GetGlobalProperties();

        // Set the wl_display pointer to be the same as the window creator's
        // See https://wiki.libsdl.org/SDL3/README-wayland#importing-external-surfaces-into-sdl-windows
        if (platformParams.platform == Platform.WAYLAND) {
            System.out.println("Using wl_display pointer " + platformParams.waylandDisplayPointer);
            SDLProperties.SDL_SetPointerProperty(globalProps, SDLVideo.SDL_PROP_GLOBAL_VIDEO_WAYLAND_WL_DISPLAY_POINTER, platformParams.waylandDisplayPointer);
        }

        boolean init = SDLInit.SDL_Init(SDLInit.SDL_INIT_VIDEO | SDLInit.SDL_INIT_EVENTS);

        if (!init) {
            System.err.println("Failed to initialize SDL Video: " + SDLError.SDL_GetError());
        }


        int windowProps = SDLProperties.SDL_CreateProperties();
        SDLProperties.SDL_SetNumberProperty(windowProps, SDLVideo.SDL_PROP_WINDOW_CREATE_HEIGHT_NUMBER, 720);
        SDLProperties.SDL_SetNumberProperty(windowProps, SDLVideo.SDL_PROP_WINDOW_CREATE_WIDTH_NUMBER, 1280);
        SDLProperties.SDL_SetBooleanProperty(windowProps, SDLVideo.SDL_PROP_WINDOW_CREATE_RESIZABLE_BOOLEAN, true);

        switch (platformParams.platform) {
            case WIN32 ->
                    SDLProperties.SDL_SetPointerProperty(windowProps, SDLVideo.SDL_PROP_WINDOW_CREATE_WIN32_HWND_POINTER, platformParams.nativeWindowPointer);
            case WAYLAND ->
                SDLProperties.SDL_SetPointerProperty(windowProps, SDLVideo.SDL_PROP_WINDOW_CREATE_WAYLAND_WL_SURFACE_POINTER, platformParams.nativeWindowPointer);
            case XORG ->
                    SDLProperties.SDL_SetPointerProperty(windowProps, SDLVideo.SDL_PROP_WINDOW_CREATE_X11_WINDOW_NUMBER, platformParams.nativeWindowPointer);
            case COCOA ->
                    SDLProperties.SDL_SetPointerProperty(windowProps, SDLVideo.SDL_PROP_WINDOW_CREATE_COCOA_WINDOW_POINTER, platformParams.nativeWindowPointer);
        }

        long sdlWindow = SDLVideo.SDL_CreateWindowWithProperties(windowProps);

        long sdlRenderer = SDLRender.SDL_CreateRenderer(sdlWindow, "");

        if (sdlRenderer == 0) {
            System.err.println(SDLError.SDL_GetError());
        }

        window: while (true) {
            SDL_Event event = SDL_Event.create();
            SDLEvents.SDL_PollEvent(event);

            switch (event.type()) {
                case SDLEvents.SDL_EVENT_QUIT: {
                    break window;
                }
                default: {
                }
            }

            SDLRender.SDL_SetRenderDrawColor(sdlRenderer, (byte) 255, (byte) 0, (byte) 127, (byte) 255);
            SDLRender.SDL_RenderClear(sdlRenderer);
            SDLRender.SDL_RenderPresent(sdlRenderer);

            Thread.sleep(10);
        }

        SDLVideo.SDL_DestroyWindow(sdlWindow);
        SDLRender.SDL_DestroyRenderer(sdlRenderer);
        SDLInit.SDL_Quit();
    }
}
