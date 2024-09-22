#include<bits/stdc++.h>
using namespace std;
#define IOS ios::sync_with_stdio(0),cin.tie(0),cout.tie(0)
#define LL long long
const LL maxn=2000050;
const long double pi = acos(-1.0);
const long double eps = 1e-18;
bool dcmp(long double x) {return (x > eps) - (x < -eps);}
inline long double min(long double a, long double b) {return a < b ? a : b;}
inline long double MAX(long double a, long double b) {return a > b ? a : b;}
inline long double Sqr(long double x) {return x * x;}
struct Point
{
    /**********一般都要用的**********/
    long double x, y, t;
    Point(){x = y = 0;}
    Point(long double a, long double b) {x = a, y = b;}

    inline Point operator-(const Point &b)const 
    {return Point(x - b.x, y - b.y);}

    inline bool operator<(const Point &b)const //重载<，排序要用
    {return dcmp(x - b.x) ? x < b.x : y < b.y;}

    inline long double dot(const Point &b)const //点积
    {return x * b.x + y * b.y;}

    inline long double Dis(const Point &b)const //距离
    {return sqrt((*this - b).dot(*this - b));}

    inline long double cross(const Point &b, const Point &c)const
    // 三点叉积，(*this).cross(b, c)右手关系为正
    {return (b.x - x) * (c.y - y) - (c.x - x) * (b.y - y);}
    
    /**********点线关系有时候用的**********/
    /*
    bool Parallel()判断平行，struct外的函数
    LineCross()直线交点，struct外的函数
    SegCross()线段交点，struct外的函数
    ToSeg()点到线段距离，声明在struct里具体定义在struct外面
    这四个里，下面的一般会调用上面的。
    注意：当精度要求较高时，尽量用原始的、相互调用少的方式验证。比如
    判断直线与线段交点，用叉积判断是否有交点后计算交点，精度高于先把线段当直线计算直线交点后再用InSeg判断是否在线段上。
    */
    long double ToSeg(const Point&, const Point&)const; //点到线段距离*/

    inline Point operator+(const Point &b)const
    {return Point(x + b.x, y + b.y);}
    inline Point operator*(const long double &b)const //x、y扩大常数倍
    {return Point(x * b, y * b);}
    inline Point operator-()
    {return Point(-x, -y);}

    inline bool InLine(const Point &b, const Point &c)const //三点共线
    {return !dcmp(cross(b, c));}

    /* 注意：如果已知点与线段端点共线，只是判断在两端点范围内，最好去掉 `InLine(b, c) &&`以提高精度*/
    inline bool OnSeg(const Point &b, const Point &c)const //点在线段上，包括端点
    {return InLine(b, c) && (*this - c).dot(*this - b) < eps;}

    inline bool InSeg(const Point &b, const Point &c)const //点在线段上，不包括端点
    {return InLine(b, c) && (*this - c).dot(*this - b) < -eps;}

    /**********其他。需要了可添加**********/
    inline bool operator>(const Point &b)const
    {return b < *this;}

    inline bool operator==(const Point &b)const
    {return !dcmp(x - b.x) && !dcmp(y - b.y);}

    Point RotePoint(const Point &p, long double ang) //p绕*this逆时针旋转ang弧度
    {
        return Point((p.x - x) * cos(ang) - (p.y - y) * sin(ang) + x,
                (p.x - x) * sin(ang) + (p.y - y) * cos(ang) + y);
    }
};
LL Graham(Point p[], int n, Point res[], LL &top) //求凸包,结果为逆时针顺序
{
    int len, i;
    top = 1;
    if(n < 2) {res[0] = p[0]; return 1;}
    sort(p, p + n);
    res[0] = p[0], res[1] = p[1];
    for(i = 2; i < n; ++ i)
    {
        while(top && res[top - 1].cross(res[top], p[i]) <= 0)
            -- top;
        res[++ top] = p[i];
    }
    len = top;
    res[++ top] = p[n - 2];
    for(i = n - 3; i >= 0; -- i)
    {
        while(top != len && res[top - 1].cross(res[top], p[i]) <= 0)
            -- top;
        res[++ top] = p[i];
    }
    return top;
}
LL n,cnt=-1,top=0;
long double a,b,r,t,anss=0;
Point s[500050],p[500050],ans[500050];
int main(){ 
	IOS;
    cin>>n;
    cin>>a>>b>>r;
    for(int i=1;i<=n;i++){
        cin>>s[i].x>>s[i].y>>s[i].t;
        int res = cnt+1;
        if(r == 0){
            p[++cnt].x = s[i].x + b/2;
            p[cnt].y = s[i].y + a/2;

            p[++cnt].x = s[i].x - b/2;
            p[cnt].y = s[i].y + a/2;

            p[++cnt].x = s[i].x + b/2;
            p[cnt].y = s[i].y - a/2;

            p[++cnt].x = s[i].x - b/2;
            p[cnt].y = s[i].y - a/2; 
        }
        else{
            p[++cnt].x = s[i].x + b/2 - r;
            p[cnt].y = s[i].y + a/2 - r; 

            p[++cnt].x = s[i].x - b/2 + r;
            p[cnt].y = s[i].y + a/2 - r; 

            p[++cnt].x = s[i].x + b/2 - r;
            p[cnt].y = s[i].y - a/2 + r; 

            p[++cnt].x = s[i].x - b/2 + r;
            p[cnt].y = s[i].y - a/2 + r; 
            
            
        }
        // for(int j=res;j<=cnt;j++){
        //     cout<<p[j].x<<' '<<p[j].y<<"  ";
        // }
        // cout<<"\n";
        for(int j=res;j<=cnt;j++){
            p[j]=s[i].RotePoint(p[j],s[i].t);
        }
        // for(int j=res;j<=cnt;j++){
        //     cout<<p[j].x<<' '<<p[j].y<<"  ";
        // }
        // cout<<"\n";
        
    }
    // for(int i=0;i<=cnt;i++){
    //     cout<<p[i].x<<' '<<p[i].y<<"\n";
    // }
    
    top=Graham(p,cnt+1,ans,top);
    
    for(int i=1;i<=top;i++){
        anss += ans[i-1].Dis(ans[i]);
        // cout<<ans[i].x<<' '<<ans[i].y<<"\n";
    }
    cout<<fixed<<setprecision(2)<<anss+2*pi*r;
	return 0;
}