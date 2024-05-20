def out_test(out integer a): void {
    a << a + 1;
}

start:
def main(): void {
    var a << 1;
    out_test(a+1);
    ("Hello world!") -->!;
}