import pyautogui
import time

time.sleep(5)  # Gives you 5 seconds to switch to the Instagram chat

for _ in range(60):  # Runs for 5 minutes (60 times, assuming 5-second intervals)
    pyautogui.typewrite("OK")
    pyautogui.press("enter")
    time.sleep(5)  # Adjust the delay as needed
