function [car_state, distance_to_goal, pid_next] = car_control(trajectory, vehicle, pid_controller, dt)
    % 输入参数：
    %   trajectory   - 结构体，包含目标路径的采样点：
    %                  .cx - x 坐标数组
    %                  .cy - y 坐标数组
    %   vehicle      - 结构体，表示车辆当前状态：
    %                  .x, .y       - 车辆位置坐标 [m]
    %                  .theta       - 车辆航向角 [rad]
    %                  .speed       - 车辆速度 [m/s]
    %                  .L           - 车辆轴距 [m]
    %                  .max_speed   - 最大速度 [m/s]
    %   pid_controller - 结构体，PID 控制器参数：
    %                    .Kp, .Ki, .Kd - PID 增益
    %                    .target_speed - 当前目标速度 [m/s]
    %                    .integral     - 积分项
    %                    .prev_error   - 上一次误差
    %   dt           - 仿真时间步长 [s]
    %
    % 输出参数：
    %   car_state        - 结构体，更新后的车辆状态（与输入参数vehicle相同结构）
    %   pid_next         - 更新后的 PID 控制器结构体
    %
    % 功能：
    %   1. 计算车辆与目标点之间的距离，并根据距离调整目标速度。
    %   2. 利用 Pure Pursuit 算法计算转向角 delta。
    %   3. 使用 PID 控制器计算车辆加速度。
    %   4. 根据转向角和加速度更新车辆位置、速度和航向角。
    %   5. 将车辆的状态及轨迹历史记录输出。

    % 计算目标点的距离并更新目标速度
    distance_to_goal = sqrt((trajectory.cx(end) - vehicle.x)^2 + (trajectory.cy(end) - vehicle.y)^2);
    if distance_to_goal < 3.0
        pid_controller.target_speed = 0;  % 接近终点时停止
    end

    % Pure Pursuit 转向角
    delta = pure_pursuit_control(vehicle, trajectory);

    % PID 速度控制
    [pid_next, acceleration] = pid_speed_control(pid_controller, vehicle.speed, dt);

    % 更新车辆状态
    car_state = update_vehicle(vehicle, delta, acceleration, dt);
end

function delta = pure_pursuit_control(vehicle, trajectory)
    pure_pursuit = struct('k', 0.1, 'max_lookahead', 30.0, 'min_lookahead', 1.5, 'L', 2.9);

    % 计算 Pure Pursuit 转向角
    cx = trajectory.cx;
    cy = trajectory.cy;
    lookahead_distance = max(pure_pursuit.min_lookahead, min(pure_pursuit.k * vehicle.speed, pure_pursuit.max_lookahead));

    % 找到最近点
    distances = sqrt((cx - vehicle.x).^2 + (cy - vehicle.y).^2);
    [~, closest_idx] = min(distances);

    % 找到 lookahead 点
    target_idx = closest_idx;
    for i = closest_idx:length(cx)
        if distances(i) >= lookahead_distance
            target_idx = i;
            break;
        end
    end

    % 计算转向角
    target_x = cx(target_idx);
    target_y = cy(target_idx);
    alpha = atan2(target_y - vehicle.y, target_x - vehicle.x) - vehicle.theta;
    delta = atan2(2 * vehicle.L * sin(alpha), lookahead_distance);
end

function [pid_next, acceleration] = pid_speed_control(pid_controller, current_speed, dt)
    % PID 速度控制器
    error = pid_controller.target_speed - current_speed;
    pid_controller.integral = pid_controller.integral + error * dt;
    derivative = (error - pid_controller.prev_error) / dt;
    pid_controller.prev_error = error;
    acceleration = pid_controller.Kp * error + pid_controller.Ki * pid_controller.integral + pid_controller.Kd * derivative;
    pid_next = pid_controller;
end

function vehicle = update_vehicle(vehicle, delta, acceleration, dt)
    % 更新车辆状态
    vehicle.speed = vehicle.speed + acceleration * dt;
    vehicle.speed = min(vehicle.speed, vehicle.max_speed);  % 限制最大速度
    vehicle.x = vehicle.x + vehicle.speed * cos(vehicle.theta) * dt;
    vehicle.y = vehicle.y + vehicle.speed * sin(vehicle.theta) * dt;
    vehicle.theta = vehicle.theta + vehicle.speed / vehicle.L * tan(delta) * dt;
end

