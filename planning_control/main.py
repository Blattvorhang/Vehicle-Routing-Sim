import matlab.engine
from system import System


def main():
    print("Starting MATLAB Engine...")
    engine = matlab.engine.start_matlab()
    
    print("Connecting to server...")
    # server_ip = "192.168.43.239"
    server_ip = "localhost"
    server_port = 8888
    system = System(engine, server_ip, server_port)
    system.init_backend()
    
    print("Running system...")
    system.run()
    
    engine.quit()


if __name__ == "__main__":
    main()
