# Understanding the Shuffle Code (Affine Modular Arithmetic)

# 1. Problem Setup

We have:

- A massive deck size:

  ```
  n = 119315717514047
  ```

- A number of shuffle repetitions:

  ```
  M = 101741582076661
  ```

- We want to find **which card ends up in position 2020** after repeating the shuffle instructions `M` times.

We cannot simulate this directly, so we use math.

---

# 2. Each Shuffle Is a Linear Function

Every shuffle maps a card position `x` to:

```
f(x) = a x + b (mod n)
```

---

# 3. Mapping Shuffle Instructions to Linear Functions

### **deal into new stack**

Reverses the deck:

```
x → -x - 1
```

So:

```
a = -1
b = -1
```

---

### **deal with increment k**

Multiplies positions by `k`:

```
x → kx
```

So:

```
a = k
b = 0
```

---

### **cut k**

Moves top `k` cards to bottom:

```
x → x - k
```

So:

```
a = 1
b = -k
```

---

# 4. Composing Linear Functions

If:

```
f(x) = fa x + fb
g(x) = ga x + gb
```

Then their composition is:

```
g(f(x)) = ga (fa x + fb) + gb
        = (ga fa)x + (ga fb + gb)
```

Thus, updating the coefficients:

```
a' = ga fa (mod n)
b' = ga fb + gb (mod n)
```

---

# 5. Repeating the Shuffle M Times

One shuffle:

```
f(x) = ax + b
```

Apply twice:

```
f(f(x)) = a² x + b(a + 1)
```

Apply M times:

```
f^M(x) = a^M x + b (1 + a + a² + ... + a^(M-1))
```

The geometric sum is:

```
1 + a + ... + a^(M-1) = (a^M - 1) / (a - 1)  (mod n)
```

Thus define:

```
f^M(x) = Ma x + Mb
Ma ≡ a^M (mod n)
Mb ≡ b * (Ma - 1) / (a - 1, n) (mod n)
```

---

# 6. Inverting the Shuffle

We want:

> **Which card ends up in position 2020?**

Given:

```
final_position = Ma * x + Mb
```

We solve:

```
x ≡ (final - Mb) * inverse(Ma)   (mod n)
```


---

# 7. Modular Inverses with Fermat’s Little Theorem

Since `n` is prime:

```
a^(n-1) ≡ 1 (mod n)
```

Giving that:
```
a^(n-1) ≡ a^(n-2) * a (mod n)
1 ≡ a^(n-2) * a (mod n)
```

We have finally:
```
a^(-1) ≡ a^(n-2) (mod n)
```

---

# 8. How to resolve both parts of today's puzzle?

For part 1, since cards values are their original index in the deck, getting the position of card 2019 is the result 
of `f^M(x) = Ma x + Mb` with x being 2019.

For part 2, we need to invert the result because we want to find the number at position 2020. So we need to find x which
satisfy `f^M(x) = 2020`

```
2020 = Ma x + Mb (mod n)
x ≡ (2020 - Mb) * inverse(Ma) (mod n)
```
