#pragma once

#include <jank/native_box.hpp>

namespace jank::runtime::behavior
{ struct sequence; }

namespace jank::runtime::behavior
{
  struct consable : virtual object
  {
    virtual ~consable() = default;
    virtual native_box<consable> cons(object_ptr head) const = 0;
  };
}
