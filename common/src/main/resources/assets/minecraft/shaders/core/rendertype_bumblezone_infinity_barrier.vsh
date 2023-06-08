#version 150

#moj_import <projection.glsl>

in vec3 Position;
in vec4 Color;
in vec3 Normal;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform mat3 IViewRotMat;

out vec3 view;
out vec3 uv3d;
out vec4 texProj0;
out vec4 vertexColor;

vec4 ModelPos = vec4(Position, 1.0);
mat4 ICamJiggleMat = mat4(inverse(mat3(ProjMat))) * ProjMat;

vec3 InitialDirection = vec3(0.0, 0.0, 1.0);
vec3 RotationAxis = cross(InitialDirection, Normal);
float AngleToRotate = acos(dot(InitialDirection, Normal));
float AngleToRotateHalfSin = sin(AngleToRotate / 2);
vec4 Quaternion = vec4(
    RotationAxis.x * AngleToRotateHalfSin,
    RotationAxis.y * AngleToRotateHalfSin,
    RotationAxis.z * AngleToRotateHalfSin,
    cos(AngleToRotate / 2)
);

float x2 = Quaternion.x * Quaternion.x;
float y2 = Quaternion.y * Quaternion.y;
float z2 = Quaternion.z * Quaternion.z;
float xy = Quaternion.x * Quaternion.y;
float xz = Quaternion.x * Quaternion.z;
float yz = Quaternion.y * Quaternion.z;
float wx = Quaternion.w * Quaternion.x;
float wz = Quaternion.w * Quaternion.z;
float wy = Quaternion.w * Quaternion.y;

mat3 Rotate = mat3(
   1.0f - 2.0f * (y2 + z2), 2.0f * (xy - wz), 2.0f * (xz + wy),
   2.0f * (xy + wz), 1.0f - 2.0f * (x2 + z2), 2.0f * (yz - wx),
   2.0f * (xz - wy), 2.0f * (yz + wx), 1.0f - 2.0f * (x2 + y2)
);

void main() {
    ICamJiggleMat[2].w = 0.0;
    ICamJiggleMat[3].z = 0.0;

    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    view = Rotate * IViewRotMat * (ICamJiggleMat * ModelPos).xyz;
    vertexColor = Color;
}
